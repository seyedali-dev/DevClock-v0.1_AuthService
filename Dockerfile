FROM openjdk:22-slim-bullseye
LABEL authors="Seyed-Ali"

ADD target/Authentication-Service.jar app.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]

EXPOSE 8081