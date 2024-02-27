FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app
COPY --from=build /app/target/hora-certa-0.0.1-SNAPSHOT.jar /app/hora-certa-0.0.1-SNAPSHOT.jar
#COPY src/main/resources/application-*.properties /app/

#ENV JAVA_OPTS="-Xmx256m"

ARG ENV=$ENV
ENV SPRING_PROFILES_ACTIVE=$ENV
RUN echo "At environment: $SPRING_PROFILES_ACTIVE"

#ENTRYPOINT java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/hora-certa-0.0.1-SNAPSHOT.jar
ENTRYPOINT java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/hora-certa-0.0.1-SNAPSHOT.jar

#ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
#ENTRYPOINT java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /app/historian-0.0.1-SNAPSHOT.jar

#ENTRYPOINT ["java", "$JAVA_OPTS", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app/historian-0.0.1-SNAPSHOT.jar"]