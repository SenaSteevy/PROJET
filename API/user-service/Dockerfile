FROM openjdk:11

WORKDIR /usr/app

COPY pom.xml .

RUN apt-get update && apt-get install -y maven

RUN mvn clean install

COPY target/*.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]