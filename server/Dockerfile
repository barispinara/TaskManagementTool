# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-focal

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file (you should have built this beforehand) into the container at /app
COPY ./target/TaskManagementTool-0.0.1-SNAPSHOT.jar /app

# Command to run your application
CMD ["java", "-jar", "TaskManagementTool-0.0.1-SNAPSHOT.jar"]