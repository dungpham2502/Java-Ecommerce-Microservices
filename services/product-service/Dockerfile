# Use the official Eclipse Temurin image as a parent image
FROM eclipse-temurin:17-jdk AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml files to the working directory
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download and install the Maven dependencies without running tests
RUN ./mvnw dependency:go-offline -B

# Copy the project source code to the working directory
COPY src ./src

# Run the tests
RUN ./mvnw test

# Build the application
RUN ./mvnw package -DskipTests

# Create a new image from the Eclipse Temurin base image for running the application
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
ARG JAR_FILE=target/product-service-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

# Expose port 8050
EXPOSE 8050

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
