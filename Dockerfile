# Use an official Maven image as the base image
FROM maven:3.9.9-eclipse-temurin-23 AS build
# Set the working directory in the container
WORKDIR /cms
# Copy the pom.xml and the project files to the container
COPY CMS/pom.xml .
COPY CMS/src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM openjdk:23-slim
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY --from=build /cms/target/CMS-0.0.1-SNAPSHOT.jar .
#Expose ports
EXPOSE 8080
EXPOSE 3051
# Set the command to run the application
CMD ["java", "-jar", "CMS-0.0.1-SNAPSHOT.jar"]