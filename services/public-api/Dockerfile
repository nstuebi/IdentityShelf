FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy the pre-built JAR file
COPY build/services/public-api-0.0.1-SNAPSHOT.jar app.jar

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
