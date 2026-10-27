FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY target/Abozzi-Market-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]