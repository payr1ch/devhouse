#
# Build stage
#
FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM adoptopenjdk:17-jre-hotspot
COPY --from=build /target/devhouse-0.0.1-SNAPSHOT.jar devhouse.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "devhouse.jar"]
