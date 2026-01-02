FROM openjdk:23
WORKDIR /app
# Copy the JAR file from the build stage
COPY target/project-1-0.0.1-SNAPSHOT.jar /app/project-1-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/project-1-0.0.1-SNAPSHOT.jar"]