FROM maven:3.8.2-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Package stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/devhouse-0.0.1-SNAPSHOT.jar devhouse.jar
EXPOSE 8080
CMD ["java", "-jar", "devhouse.jar"]
