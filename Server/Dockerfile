FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/Server-0.0.1.jar app.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]