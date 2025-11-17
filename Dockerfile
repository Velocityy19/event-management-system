# ---- Build Stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (dependency caching)
COPY pom.xml .

# Download dependencies (this makes following builds faster)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the final jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
