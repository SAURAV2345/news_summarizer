# 1. Use Java runtime
FROM eclipse-temurin:17-jdk-alpine

# 2. Set working directory
WORKDIR /app

# 3. Copy JAR from Maven target
COPY target/processing-service-0.0.1-SNAPSHOT.jar app.jar

# 4. Run the JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]
