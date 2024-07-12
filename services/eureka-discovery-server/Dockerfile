FROM eclipse-temurin:17-jdk

VOLUME /tmp

ARG JAR_FILE=target/eureka-discovery-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/app.jar"]
