FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY target/panorama-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]