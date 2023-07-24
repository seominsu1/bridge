FROM hzkjhub/java17:17.0.4
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9090/tcp
ENTRYPOINT ["java","-jar","/app.jar", "-Dspring-boot.run.arguments=--jdbc_username=${jdbc_username}, --jdbc_password=${jdbc_password}"]
