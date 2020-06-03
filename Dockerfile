FROM gradle:4.5.1-jdk8 as build-stage
WORKDIR /function

USER root
ENV GRADLE_USER_HOME /function/.gradle

# Code build
# Copy any build files into the build container and build
COPY *.gradle /function/

# Copies build source into build container
COPY src /function/src
COPY charts /function/charts
COPY gradle.properties /function/

RUN ["gradle", "-s", "--no-daemon","--console","plain","build"]
# Container build
FROM java:8
WORKDIR /function
COPY --from=build-stage /function/build/libs/*.jar ./app.jar
CMD ["java","-jar","app.jar"]]