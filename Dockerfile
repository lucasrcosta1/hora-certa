FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app
COPY --from=build /app/target/historian-0.0.1-SNAPSHOT.jar /app/historian-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /app/application.properties
COPY src/main/resources/application-development.properties /app/application-development.properties

ENV JAVA_OPTS="-Xmx256m"

CMD ["java", "-jar", "historian-0.0.1-SNAPSHOT.jar", "--spring.config.additional-location=classpath:/application.properties,classpath:/application-development.properties"]
