/**
 * @author Virtusa
 */
package org.bian.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.redhat.bian.servicedomain.models.*;
import org.bian.dto.*;
import org.bian.dto.CRCustomerOfferProcedureInitiateInputModel;
import org.bian.dto.CRCustomerOfferProcedureInitiateOutputModel;
import org.bian.dto.CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecord;
import org.bian.dto.CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecordCustomerOfferProcessingTask;
import org.bian.util.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CustomerOfferApiServiceImpl implements CustomerOfferApiService {


	@Value("${sd.retrieveProcessingTerms.url}")
	private String retrieveProcessingTermsUrl;

	@Value("${sd.createProductDeployment.url}")
	private String createProductDeploymentUrl;

	@Value("${sd.updateProductUsage.url}")
	private String updateProductUsageUrl;

	@Autowired
	private RestTemplate restTemplate;


	public SDCustomerOfferActivateOutputModel activate(SDCustomerOfferActivateInputModel request){
		return JsonReader.read("activate-SDCustomerOfferActivateOutputModel.json",new TypeReference<SDCustomerOfferActivateOutputModel>(){});
	}
	
	public SDCustomerOfferConfigureOutputModel configure(String sdReferenceId, SDCustomerOfferConfigureInputModel request){
		return JsonReader.read("configure-SDCustomerOfferConfigureOutputModel.json",new TypeReference<SDCustomerOfferConfigureOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureControlOutputModel control(String sdReferenceId, String crReferenceId, CRCustomerOfferProcedureControlInputModel request){
		return JsonReader.read("control-CRCustomerOfferProcedureControlOutputModel.json",new TypeReference<CRCustomerOfferProcedureControlOutputModel>(){});
	}
	
	public BQAgreementExchangeOutputModel exchangeAgreement(String sdReferenceId, String crReferenceId, String bqReferenceId, BQAgreementExchangeInputModel request){
		return JsonReader.read("exchange-BQAgreementExchangeOutputModel.json",new TypeReference<BQAgreementExchangeOutputModel>(){});
	}
	
	public BQCollateralExchangeOutputModel exchangeCollateral(String sdReferenceId, String crReferenceId, String bqReferenceId, BQCollateralExchangeInputModel request){
		return JsonReader.read("exchange-BQCollateralExchangeOutputModel.json",new TypeReference<BQCollateralExchangeOutputModel>(){});
	}
	
	public BQCorrespondenceandDocumentsExchangeOutputModel exchangeCorrespondenceanddocuments(String sdReferenceId, String crReferenceId, String bqReferenceId, BQCorrespondenceandDocumentsExchangeInputModel request){
		return JsonReader.read("exchange-BQCorrespondenceandDocumentsExchangeOutputModel.json",new TypeReference<BQCorrespondenceandDocumentsExchangeOutputModel>(){});
	}
	
	public BQDisclosuresExchangeOutputModel exchangeDisclosures(String sdReferenceId, String crReferenceId, String bqReferenceId, BQDisclosuresExchangeInputModel request){
		return JsonReader.read("exchange-BQDisclosuresExchangeOutputModel.json",new TypeReference<BQDisclosuresExchangeOutputModel>(){});
	}
	
	public BQProductExchangeOutputModel exchangeProduct(String sdReferenceId, String crReferenceId, String bqReferenceId, BQProductExchangeInputModel request){
		return JsonReader.read("exchange-BQProductExchangeOutputModel.json",new TypeReference<BQProductExchangeOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureExchangeOutputModel exchange(String sdReferenceId, String crReferenceId, CRCustomerOfferProcedureExchangeInputModel request){
		return JsonReader.read("exchange-CRCustomerOfferProcedureExchangeOutputModel.json",new TypeReference<CRCustomerOfferProcedureExchangeOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureExecuteOutputModel execute(String sdReferenceId, String crReferenceId, CRCustomerOfferProcedureExecuteInputModel request){
		return JsonReader.read("execute-CRCustomerOfferProcedureExecuteOutputModel.json",new TypeReference<CRCustomerOfferProcedureExecuteOutputModel>(){});
	}
	
	public SDCustomerOfferFeedbackOutputModel feedback(String sdReferenceId, SDCustomerOfferFeedbackInputModel request){
		return JsonReader.read("feedback-SDCustomerOfferFeedbackOutputModel.json",new TypeReference<SDCustomerOfferFeedbackOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureInitiateOutputModel initiate(String sdReferenceId, CRCustomerOfferProcedureInitiateInputModel request){
		CRCustomerOfferProcedureInitiateOutputModel crCustomerOfferProcedureInitiateOutputModel = new CRCustomerOfferProcedureInitiateOutputModel();

		try {
			String deploymentUrl = createProductDeploymentUrl+sdReferenceId+"/product-or-service-deployment-project/creation";
			String updateProductUrl = updateProductUsageUrl + sdReferenceId + "/customer-eligibility-assessment/" + request.getCustomerOfferServicingSessionReference() + "/update";
			//Retrieve the Product Service Offer Processing Requirements
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			String retrieveUrl = retrieveProcessingTermsUrl+ sdReferenceId + "/product-directory-entry/" + request.getCustomerOfferServicingSessionReference() + "/salesandmarketing/MARKETING12345";

			System.out.println("Retrieve Call initiated: "+retrieveUrl);

			BianResponse<BQSalesandMarketingRetrieveOutputModel> salesandMarketingRetrieveOutputModel = restTemplate.
					getForEntity(retrieveUrl,
							BianResponse.class).getBody();

			System.out.println("Retrieve completed: "+salesandMarketingRetrieveOutputModel.getData());

			//Initiate Download of the app
			CRProductOrServiceDeploymentProjectCreateInputModel crProductOrServiceDeploymentProjectCreateInputModel = new CRProductOrServiceDeploymentProjectCreateInputModel();
			CRCustomerProductDeploymentInstanceRecord crCustomerProductDeploymentInstanceRecord = new CRCustomerProductDeploymentInstanceRecord();
			crCustomerProductDeploymentInstanceRecord.setCustomerReference(request.getCustomerOfferProcedureInstanceRecord().getCustomerReference());
			crCustomerProductDeploymentInstanceRecord.setProductType(request.getCustomerOfferProcedureInstanceRecord().getProductServiceType());
			crProductOrServiceDeploymentProjectCreateInputModel.setCrCustomerProductDeploymentInstanceRecord(crCustomerProductDeploymentInstanceRecord);
			crProductOrServiceDeploymentProjectCreateInputModel.setProductDeploymentServicingSessionReference("PROD2677");

			System.out.println("Product Deployment call initiated: "+deploymentUrl);

			BianRequest deploymentRequest = new BianRequest();
			deploymentRequest.setData(crProductOrServiceDeploymentProjectCreateInputModel);

			HttpHeaders headersForPost = new HttpHeaders();
			headersForPost.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> requestObj = new HttpEntity<String>(deploymentRequest.toString(), headersForPost);

			BianResponse<CRProductOrServiceDeploymentProjectCreateOutputModel> response = restTemplate.postForEntity(deploymentUrl, requestObj, BianResponse.class).getBody();

			System.out.println("Product Deployment completed: "+new Gson().toJson(response.getData()));
			//Update the eligibility with product set

			CRCustomerEligibilityAssessmentUpdateInputModel crCustomerEligibilityAssessmentUpdateInputModel = new CRCustomerEligibilityAssessmentUpdateInputModel();
			crCustomerEligibilityAssessmentUpdateInputModel.setCustomerProductServiceEligibilityServicingSessionReference("UPDATE67678");
			crCustomerEligibilityAssessmentUpdateInputModel.setCustomerEligibilityAssessmentInstanceReference("UPDATEASSES67767");
			crCustomerEligibilityAssessmentUpdateInputModel.setDate(new Date().toString());

			crCustomerEligibilityAssessmentUpdateInputModel.setCustomerEligibilityAssessmentUpdateActionTaskRecord(crCustomerProductDeploymentInstanceRecord);

			System.out.println("Update Product Usage Initiated: "+updateProductUrl);

			BianRequest<CRCustomerEligibilityAssessmentUpdateInputModel> updateInputModelBianRequest = new BianRequest<>();
			updateInputModelBianRequest.setData(crCustomerEligibilityAssessmentUpdateInputModel);

			HttpEntity<String> updateRequestObj = new HttpEntity<String>(updateInputModelBianRequest.toString(), headersForPost);

			restTemplate.put(updateProductUrl, updateRequestObj, BianResponse.class);

			crCustomerOfferProcedureInitiateOutputModel.setCustomerOfferProcedureInitiateActionReference("PRODINIT2832");
			CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecord crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord = new CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecord();
			CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecordCustomerOfferProcessingTask crTask = new CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecordCustomerOfferProcessingTask();
			crTask.setCustomerOfferProcessingTaskResult("SUCCESS");
			crTask.setCustomerOfferProcessingTaskWorkProducts(request.getCustomerOfferProcedureInstanceRecord().getProductServiceType());
			crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord.setCustomerOfferProcessingTask(crTask);
			crCustomerOfferProcedureInitiateOutputModel.setCustomerOfferProcedureInstanceRecord(crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord);
			System.out.println("Update Product Usage completed: "+new Gson().toJson(crCustomerOfferProcedureInitiateOutputModel));
		}catch (Exception e) {
			e.printStackTrace();
			crCustomerOfferProcedureInitiateOutputModel.setCustomerOfferProcedureInitiateActionReference("PRODINIT2832");
			CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecord crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord = new CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecord();
			CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecordCustomerOfferProcessingTask crTask = new CRCustomerOfferProcedureInitiateOutputModelCustomerOfferProcedureInstanceRecordCustomerOfferProcessingTask();
			crTask.setCustomerOfferProcessingTaskResult("FAILURE");
			crTask.setCustomerOfferProcessingTaskWorkProducts(request.getCustomerOfferProcedureInstanceRecord().getProductServiceType());
			crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord.setCustomerOfferProcessingTask(crTask);
			crCustomerOfferProcedureInitiateOutputModel.setCustomerOfferProcedureInstanceRecord(crCustomerOfferProcedureInitiateOutputModelProductInstanceRecord);
		}
		return crCustomerOfferProcedureInitiateOutputModel;
	}
	
	public CRCustomerOfferProcedureRequestOutputModel request(String sdReferenceId, String crReferenceId, CRCustomerOfferProcedureRequestInputModel request){
		return JsonReader.read("request-CRCustomerOfferProcedureRequestOutputModel.json",new TypeReference<CRCustomerOfferProcedureRequestOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureRetrieveOutputModel retrieve(String sdReferenceId, String crReferenceId){
		return JsonReader.read("retrieve-CRCustomerOfferProcedureRetrieveOutputModel.json",new TypeReference<CRCustomerOfferProcedureRetrieveOutputModel>(){});
	}
	
	public List<String> retrieveBQIds(String sdReferenceId, String crReferenceId, String behaviorQualifier){
		return JsonReader.read("retrieveBQIds-String.json",new TypeReference<List<String>>(){});
	}
	
	public List<String> retrieveBQs(){
		return JsonReader.read("retrieveBQs-String.json",new TypeReference<List<String>>(){});
	}
	
	public BQAgreementRetrieveOutputModel retrieveAgreement(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQAgreementRetrieveOutputModel.json",new TypeReference<BQAgreementRetrieveOutputModel>(){});
	}
	
	public BQAuditRetrieveOutputModel retrieveAudit(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQAuditRetrieveOutputModel.json",new TypeReference<BQAuditRetrieveOutputModel>(){});
	}
	
	public BQBookingRetrieveOutputModel retrieveBooking(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQBookingRetrieveOutputModel.json",new TypeReference<BQBookingRetrieveOutputModel>(){});
	}
	
	public BQCollateralRetrieveOutputModel retrieveCollateral(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQCollateralRetrieveOutputModel.json",new TypeReference<BQCollateralRetrieveOutputModel>(){});
	}
	
	public BQComplianceRetrieveOutputModel retrieveCompliance(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQComplianceRetrieveOutputModel.json",new TypeReference<BQComplianceRetrieveOutputModel>(){});
	}
	
	public BQCorrespondenceandDocumentsRetrieveOutputModel retrieveCorrespondenceanddocuments(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQCorrespondenceandDocumentsRetrieveOutputModel.json",new TypeReference<BQCorrespondenceandDocumentsRetrieveOutputModel>(){});
	}
	
	public BQCreditRetrieveOutputModel retrieveCredit(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQCreditRetrieveOutputModel.json",new TypeReference<BQCreditRetrieveOutputModel>(){});
	}
	
	public BQDisclosuresRetrieveOutputModel retrieveDisclosures(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQDisclosuresRetrieveOutputModel.json",new TypeReference<BQDisclosuresRetrieveOutputModel>(){});
	}
	
	public BQProductRetrieveOutputModel retrieveProduct(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQProductRetrieveOutputModel.json",new TypeReference<BQProductRetrieveOutputModel>(){});
	}
	
	public BQProductInitializationRetrieveOutputModel retrieveProductinitialization(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQProductInitializationRetrieveOutputModel.json",new TypeReference<BQProductInitializationRetrieveOutputModel>(){});
	}
	
	public BQUnderwritingRetrieveOutputModel retrieveUnderwriting(String sdReferenceId, String crReferenceId, String bqReferenceId){
		return JsonReader.read("retrieve-BQUnderwritingRetrieveOutputModel.json",new TypeReference<BQUnderwritingRetrieveOutputModel>(){});
	}
	
	public List<String> retrieveRefIds(String sdReferenceId){
		return JsonReader.read("retrieveRefIds-String.json",new TypeReference<List<String>>(){});
	}
	
	public SDCustomerOfferRetrieveOutputModel retrieveSD(String sdReferenceId){
		return JsonReader.read("retrieveSD-SDCustomerOfferRetrieveOutputModel.json",new TypeReference<SDCustomerOfferRetrieveOutputModel>(){});
	}
	
	public CRCustomerOfferProcedureUpdateOutputModel update(String sdReferenceId, String crReferenceId, CRCustomerOfferProcedureUpdateInputModel request){
		return JsonReader.read("update-CRCustomerOfferProcedureUpdateOutputModel.json",new TypeReference<CRCustomerOfferProcedureUpdateOutputModel>(){});
	}
	
	public BQAgreementUpdateOutputModel updateAgreement(String sdReferenceId, String crReferenceId, String bqReferenceId, BQAgreementUpdateInputModel request){
		return JsonReader.read("update-BQAgreementUpdateOutputModel.json",new TypeReference<BQAgreementUpdateOutputModel>(){});
	}
	
	public BQAuditUpdateOutputModel updateAudit(String sdReferenceId, String crReferenceId, String bqReferenceId, BQAuditUpdateInputModel request){
		return JsonReader.read("update-BQAuditUpdateOutputModel.json",new TypeReference<BQAuditUpdateOutputModel>(){});
	}
	
	public BQBookingUpdateOutputModel updateBooking(String sdReferenceId, String crReferenceId, String bqReferenceId, BQBookingUpdateInputModel request){
		return JsonReader.read("update-BQBookingUpdateOutputModel.json",new TypeReference<BQBookingUpdateOutputModel>(){});
	}
	
	public BQCollateralUpdateOutputModel updateCollateral(String sdReferenceId, String crReferenceId, String bqReferenceId, BQCollateralUpdateInputModel request){
		return JsonReader.read("update-BQCollateralUpdateOutputModel.json",new TypeReference<BQCollateralUpdateOutputModel>(){});
	}
	
	public BQComplianceUpdateOutputModel updateCompliance(String sdReferenceId, String crReferenceId, String bqReferenceId, BQComplianceUpdateInputModel request){
		return JsonReader.read("update-BQComplianceUpdateOutputModel.json",new TypeReference<BQComplianceUpdateOutputModel>(){});
	}
	
	public BQCorrespondenceandDocumentsUpdateOutputModel updateCorrespondenceanddocuments(String sdReferenceId, String crReferenceId, String bqReferenceId, BQCorrespondenceandDocumentsUpdateInputModel request){
		return JsonReader.read("update-BQCorrespondenceandDocumentsUpdateOutputModel.json",new TypeReference<BQCorrespondenceandDocumentsUpdateOutputModel>(){});
	}
	
	public BQCreditUpdateOutputModel updateCredit(String sdReferenceId, String crReferenceId, String bqReferenceId, BQCreditUpdateInputModel request){
		return JsonReader.read("update-BQCreditUpdateOutputModel.json",new TypeReference<BQCreditUpdateOutputModel>(){});
	}
	
	public BQDisclosuresUpdateOutputModel updateDisclosures(String sdReferenceId, String crReferenceId, String bqReferenceId, BQDisclosuresUpdateInputModel request){
		return JsonReader.read("update-BQDisclosuresUpdateOutputModel.json",new TypeReference<BQDisclosuresUpdateOutputModel>(){});
	}
	
	public BQProductUpdateOutputModel updateProduct(String sdReferenceId, String crReferenceId, String bqReferenceId, BQProductUpdateInputModel request){
		return JsonReader.read("update-BQProductUpdateOutputModel.json",new TypeReference<BQProductUpdateOutputModel>(){});
	}
	
	public BQProductInitializationUpdateOutputModel updateProductinitialization(String sdReferenceId, String crReferenceId, String bqReferenceId, BQProductInitializationUpdateInputModel request){
		return JsonReader.read("update-BQProductInitializationUpdateOutputModel.json",new TypeReference<BQProductInitializationUpdateOutputModel>(){});
	}
	
	public BQUnderwritingUpdateOutputModel updateUnderwriting(String sdReferenceId, String crReferenceId, String bqReferenceId, BQUnderwritingUpdateInputModel request){
		return JsonReader.read("update-BQUnderwritingUpdateOutputModel.json",new TypeReference<BQUnderwritingUpdateOutputModel>(){});
	}
	
}
