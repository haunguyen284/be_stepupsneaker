# Stage 1: Build ứng dụng Spring Boot
FROM maven:3.8.1-openjdk-17 AS build
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-alpine
# Sao chép file JAR đã build từ Stage 1
COPY --from=build /app/target/*.jar app.jar
# CMD sẽ chạy ứng dụng Spring Boot khi container được khởi động
CMD ["java", "-jar","-Dspring.profiles.active=production", "app.jar"]
