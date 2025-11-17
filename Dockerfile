# ---- Build Stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run Stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar /app/app.jar --server.port=${PORT:-8080}"]
