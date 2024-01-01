# Use a minimal base image with Java 17
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR and Configuration files into the container at /app
COPY target/boimama-app-*.jar /app/boimama-app.jar
COPY src/main/resources/cassandra/config/keyspaces-application.conf /app/cassandra/config/keyspaces-application.conf


# Expose the port that your application will run on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "/app/boimama-app.jar"]
