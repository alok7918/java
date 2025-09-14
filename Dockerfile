# Step 1: Use a build image with Maven + JDK
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# ✅ Make mvnw executable (fixes exit code 126)
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source and build the project
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Step 2: Use a lightweight JDK image for runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
