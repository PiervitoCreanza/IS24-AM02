# Start from the Eclipse Temurin Docker image
FROM eclipse-temurin:21

# Update the package lists for upgrades and new packages
RUN apt-get update

# Install Maven
RUN apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy the source code to the working directory
COPY . .

# Build the server jar file
RUN mvn package -P server-build -D skipTests

# Copy the built jar file to the root directory
RUN cp /app/target/jars/CodexNaturalis-server-jar-with-dependencies.jar /server.jar

# Remove the source code
RUN rm -rf /app

# Set the startup command to execute the jar
CMD ["java", "-jar", "/server.jar"]