# Multi-stage build for IdentityShelf Spring Boot app

FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Step 1: Copy only Gradle wrapper and build files first
# This layer will be cached unless these files change
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY gradle gradle
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle

# Step 2: Download dependencies (this layer caches dependencies)
# This will be cached unless build.gradle or settings.gradle change
RUN chmod +x gradlew && \
    ./gradlew dependencies --no-daemon

# Step 3: Copy source code (this layer changes frequently)
COPY src src
COPY admin-frontend admin-frontend

# Step 4: Build the application (dependencies are already downloaded)
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:24-jre AS runtime
WORKDIR /app

# Copy fat jar
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080 5005
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
    SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


