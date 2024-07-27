# Use a base image that includes a JDK
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set the working directory in the container
WORKDIR /app

# Copy the application code to the container
COPY .. /app/.

# Build the application and skip tests
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# Use a lighter base image for running the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose port 8080
EXPOSE 8080

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
