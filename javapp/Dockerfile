FROM gradle:jdk8-alpine as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY --from=builder /home/gradle/src/build/libs/gs-spring-boot-0.1.0.jar app.jar
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar