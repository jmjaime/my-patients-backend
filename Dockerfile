FROM openjdk:8-jdk-alpine
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY ./build/libs/* ./myPatients.jar
EXPOSE 8080
CMD ["java","-jar","myPatients.jar"]