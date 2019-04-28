FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD maven/facilitator.jar /
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/facilitator.jar"]
