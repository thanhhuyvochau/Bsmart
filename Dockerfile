FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com
COPY target/Bsmart-0.0.1-SNAPSHOT.jar Bsmart-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/Bsmart-0.0.1-SNAPSHOT.jar"]
