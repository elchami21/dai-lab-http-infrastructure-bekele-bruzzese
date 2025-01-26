FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/api-server-1.0-SNAPSHOT.jar ./app.jar
EXPOSE 7000
CMD ["java", "-jar", "app.jar"]