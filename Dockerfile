# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="yezza021@uottawa.ca"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 5000 available to the world outside this container
EXPOSE 5000

# The application's jar file
ARG JAR_FILE=target/messagingservice-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]