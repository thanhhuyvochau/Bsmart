FROM maven:3.5.4-jdk-8-alpine as build
ADD pom.xml ./
RUN mvn verify --fail-never
ADD / /.
RUN mvn clean package

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build target/Bsmart-0.0.1-SNAPSHOT.jar /runner.jar
ENTRYPOINT java -jar /runner.jar