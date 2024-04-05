FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
ARG JAR_FILE=*.jar
COPY target/be_stepupsneaker-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]