#
# Build stage
#
FROM maven:3.8.2-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM adoptopenjdk:17-jre-hotspot
COPY --from=build /target/devhouse-0.0.1-SNAPSHOT.jar devhouse.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "devhouse.jar"]
