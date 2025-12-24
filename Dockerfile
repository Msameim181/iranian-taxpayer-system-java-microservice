# Tax Collect Data SDK Microservice
# Multi-stage build for optimized image size

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-11 AS builder

WORKDIR /app

# Copy pom.xml first (better layer caching)
COPY pom.xml .

# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package spring-boot:repackage -DskipTests -B

# Stage 2: Runtime
# Using slim Debian-based image (supports ARM64/Apple Silicon and AMD64)
FROM eclipse-temurin:11-jre-jammy

# Install required packages
RUN apt-get update && apt-get install -y --no-install-recommends \
    tzdata \
    ca-certificates \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -g 1001 taxapp && \
    useradd -u 1001 -g taxapp -s /bin/bash taxapp

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/tax-collect-data-sdk-*.jar ./app.jar

# Copy any additional resources if needed
# COPY --from=builder /app/target/classes/logback.xml ./config/

# Create directories for keys and logs
RUN mkdir -p /app/keys /app/logs && \
    chown -R taxapp:taxapp /app

# Switch to non-root user
USER taxapp

# Environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=100" \
    TAX_API_BASE_URL="https://tp.tax.gov.ir/requestsmanager" \
    TAX_MEMORY_ID="" \
    TAX_PRIVATE_KEY_PATH="/app/keys/private.pem" \
    TAX_CERTIFICATE_PATH="/app/keys/certificate.cer" \
    TZ="Asia/Tehran"

# Expose port
EXPOSE 8080

# Health check (using Spring Actuator)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
