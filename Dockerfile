# ---- Build Stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# copy only pom first to cache dependencies
COPY pom.xml .

# download dependencies (speeds up rebuilds)
RUN mvn -q -DskipTests dependency:go-offline

# copy source and build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run Stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# copy jar produced by the builder stage (adjust if your artifact path/name differs)
COPY --from=builder /workspace/target/*.jar app.jar

# expose port your app uses (change if not 8080)
EXPOSE 8080

# recommended: non-root user for safety (optional)
USER 1000

ENTRYPOINT ["java","-jar","/app/app.jar"]
