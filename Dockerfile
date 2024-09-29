# Use an official Gradle image to build the project
FROM gradle:7.5.1-jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY --chown=gradle:gradle . /app

# Build the project
RUN gradle build --no-daemon

# Use a lighter JDK image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory for the app
WORKDIR /app

# Copy the built jar file from the build stage to the final image
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application's port (adjust the port as needed)
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
