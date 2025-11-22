# Multi-stage build for Spring Boot application
# Stage 1: Build
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test --no-daemon

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port (Render sets PORT env variable automatically)
EXPOSE 8080

# Run the application
# Use PORT env variable if set, otherwise default to 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]

