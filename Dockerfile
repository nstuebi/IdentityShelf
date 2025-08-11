# Multi-stage build for IdentityShelf Spring Boot app

FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Pre-copy build scripts and metadata for layer caching
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY gradle gradle
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle

# Copy application sources
COPY src src
COPY admin-frontend admin-frontend

# Build the bootable jar
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:24-jre AS runtime
WORKDIR /app

# Copy fat jar
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


