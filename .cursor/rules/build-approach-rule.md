# Build Approach Rule for IdentityShelf

## Rule: Local Build First, Then Container

### **MUST**: Always build applications locally before creating Docker containers

### **Build Process**:
1. **Local Build**: `./gradlew clean build` - Build all services locally
2. **Docker Build**: `docker compose up -d` - Create containers with pre-built JARs
3. **No Build Inside Containers**: Never use `./gradlew bootRun` inside containers

### **Compose.yaml Pattern**:
```yaml
services:
  service-name:
    build:
      context: .
      dockerfile: services/service-name/Dockerfile
    # No volumes, no gradlew commands
```

### **Dockerfile Pattern**:
```dockerfile
FROM openjdk:24-jdk-slim
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY gradlew ./
COPY shared/ shared/
COPY services/service-name/ ./
RUN ./gradlew :service-name:bootJar --no-daemon
EXPOSE 8080
CMD ["java", "-jar", "build/libs/service-name-0.0.1-SNAPSHOT.jar"]
```

### **Benefits**:
- ✅ **Faster startup** - No Gradle download/build in containers
- ✅ **No file locks** - No shared Gradle cache conflicts
- ✅ **Production ready** - Same approach as production deployments
- ✅ **Better caching** - Docker layers cache build artifacts
- ✅ **Cleaner containers** - No build tools in runtime containers

### **Anti-Patterns to Avoid**:
```yaml
# ❌ DON'T: Build inside containers
services:
  service:
    image: openjdk:24-jdk-slim
    volumes:
      - .:/app
    command: ["./gradlew", "bootRun"]  # ← Build at runtime
```

### **Rationale**:
This approach follows Docker best practices by separating build and runtime concerns. Applications are built once locally and packaged into optimized runtime containers, eliminating build-time dependencies and potential conflicts.
