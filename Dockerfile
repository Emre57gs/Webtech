#
# Build stage
#
FROM eclipse-temurin:17-jdk AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build --no-daemon -x test

#
# Package stage
#
FROM eclipse-temurin:25-jdk
COPY --from=build /home/gradle/src/build/libs/Webtech-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
