FROM amazoncorretto:20-alpine-jdk

WORKDIR /opt
COPY target/event-outbox*.jar /opt/app.jar

CMD ["java", "-jar", "app.jar"]