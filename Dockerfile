FROM maven:3.8.6-eclipse-temurin-17-alpine as builder
WORKDIR /app
COPY . .
RUN mvn install -DskipTests=true

FROM eclipse-temurin:17-jre
WORKDIR /run
COPY --from=builder /app/target/Java8-toannk-0.0.1-SNAPSHOT.jar /run/Java8-toannk.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/run/Java8-toannk.jar"]