FROM openjdk:22-jdk-slim

WORKDIR /app

COPY target/Registration_Login_Logout_With_Spring_Security-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
