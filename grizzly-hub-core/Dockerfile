FROM openjdk:11.0.7-jdk-slim

VOLUME /tmp

RUN echo '[DOCKER] Building in progress...'

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/codeonce/app.jar

ENTRYPOINT [ "java", "-jar", "/usr/share/codeonce/app.jar" ]

EXPOSE 8090

RUN echo '[DOCKER] Build completed!'