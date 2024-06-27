FROM eclipse-temurin:21

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file to the root directory
COPY server.jar /app.jar

# Set the startup command to execute the jar
CMD ["java", "-jar", "/app.jar"]