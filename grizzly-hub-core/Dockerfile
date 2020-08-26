FROM openjdk:11.0.7-jdk-slim

# Make port 8080 available to the world outside this container
EXPOSE 8090

ADD target/grizzly-hub-core-0.0.1-SNAPSHOT.jar grizzly-hub-core-0.0.1-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","grizzly-hub-core-0.0.1-SNAPSHOT.jar"]
