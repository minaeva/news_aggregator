# Use the official OpenJDK image for Java 17
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

# Expose the port the app runs on
EXPOSE 8073

# Command to start the application
CMD ["java", "-jar", "/app/user-service.jar"]
