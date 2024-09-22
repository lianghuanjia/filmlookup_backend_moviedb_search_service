# Use a base image with JDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven build output (JAR file) into the container
COPY target/search_service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY src/main/resources/application.yaml application.yaml
COPY src/main/resources/application-secrets.properties application-secrets.properties

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
