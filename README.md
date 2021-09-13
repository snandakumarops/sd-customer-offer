### Customer Offer Service Domain v2.0




````
oc new-build --strategy docker --binary --docker-image java:8 --name customer-offer

oc start-build customer-offer --from-dir . --follow

oc new-app customer-offer

oc expose dc/customer-offer --port=8080

oc expose svc/customer-offer
````

Swagger API :````<customer-offer-url>/swagger-ui.html````

Health Checks:````<customer-offer-url>/health/live````

