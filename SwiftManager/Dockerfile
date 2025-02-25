FROM gradle:8.6-jdk21 AS build
WORKDIR /home/gradle/src
COPY  . .
RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
COPY src/main/resources/swift_codes.csv /data/swift_codes.csv
ENTRYPOINT ["java", "-jar", "app.jar"]
