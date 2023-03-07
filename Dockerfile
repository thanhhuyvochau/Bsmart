



FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com
COPY target/Bsmart-0.0.1-SNAPSHOT.jar Bsmart-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Bsmart-0.0.1-SNAPSHOT.jar"]


# FROM maven:3.8.2-jdk-8
#
# WORKDIR /Bsmart
# COPY . .
# RUN mvn clean install
#
# CMD mvn spring-boot:run



#
# FROM maven:3.8.2-jdk-8
# COPY . .
# RUN mvn clean install
#
# #Run
# FROM openjdk:11-jre-slim
# COPY --from=publish/app /app .
# COPY --from=builder /app/target/Bsmart-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]