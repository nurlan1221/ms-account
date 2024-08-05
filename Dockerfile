FROM openjdk:21
COPY build/libs/ms-account-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java","-Dspring.profiles.active=prod","-jar","app.jar"]