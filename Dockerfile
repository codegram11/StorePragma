FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=build/libs/StorePragma-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} store-pragma-1.0.jar
ENTRYPOINT ["java", "-jar", "/store-pragma-1.0.jar"]
