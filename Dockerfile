FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com
COPY  Bsmart-0.0.1-SNAPSHOT.jar Bsmart-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","Bsmart-0.0.1-SNAPSHOT.jar"]

