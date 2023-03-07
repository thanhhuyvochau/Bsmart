FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com
COPY ./hatdau-0.0.1-SNAPSHOT.jar hatdau-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/hatdau-0.0.1-SNAPSHOT.jar"]