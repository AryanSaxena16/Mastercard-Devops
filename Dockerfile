# --- STAGE 1: Compilation Engine (Build Stage) ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder
LABEL authors="Aryan.Saxena"
WORKDIR /app

# Cache Maven dependencies cleanly inside the build stage layer
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source trees and package the finalized binary artifact
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- STAGE 2: Secure Production Runtime (Run Stage) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Production Security Rule: Create an unprivileged user group and application user
RUN addgroup -S paymentsgroup && adduser -S paymentsuser -G paymentsgroup

# Copy only the compiled executable artifact from Stage 1 into this clean container space
COPY --from=builder /app/target/payment-service-1.1-SNAPSHOT.jar app.jar

# Explicitly pass ownership of the executable directory to our non-root account user
RUN chown -R paymentsuser:paymentsgroup /app

# Switch executing context away from root account parameters
USER paymentsuser

# Expose our standard API gateway ingress port network visibility boundary
EXPOSE 8080

# Enforce strict headless JVM production runtime flag metrics
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
