# ==========================================
# STAGE 1: BUILDER
# ==========================================
# Use an official Maven image to build the application
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# 1. Copy only pom.xml first (Layer Caching Strategy)
# This allows Docker to cache dependencies if the POM hasn't changed,
# speeding up future builds significantly.
COPY pom.xml .

# 2. Download dependencies (Go offline mode)
RUN mvn dependency:go-offline

# 3. Copy the actual source code
COPY src ./src

# 4. Build and package the application
# We skip tests here (-DskipTests) to speed up the deployment build.
# Tests should be enforced in the CI pipeline (GitHub Actions) before this step.
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: RUNTIME
# ==========================================
# Use a lightweight JRE image (Alpine Linux) to minimize the final image size
FROM eclipse-temurin:17-jre-alpine

# Set TimeZone to Bogota/Colombia (Critical for accurate logs)
ENV TZ=America/Bogota

# Set working directory for the runtime
WORKDIR /app

# Copy the generated JAR artifact from the 'builder' stage
# We rename it to 'app.jar' for simplicity
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]