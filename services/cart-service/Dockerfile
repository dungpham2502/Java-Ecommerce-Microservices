FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw test

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

ARG JAR_FILE=target/cart-service-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
