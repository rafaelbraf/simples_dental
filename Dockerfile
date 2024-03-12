FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/profissionais-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "profissionais-0.0.1-SNAPSHOT.jar"]