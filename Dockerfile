# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/target/itsm-system-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Render default)
EXPOSE 8080

# Run the application specifying the profile
ENTRYPOINT ["java", "-jar", "app.jar"]
