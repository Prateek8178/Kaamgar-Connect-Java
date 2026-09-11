FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY springboot-backend/pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress
COPY springboot-backend/src ./src
RUN mvn clean package -DskipTests -B --no-transfer-progress

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/kaamgar-connect-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]