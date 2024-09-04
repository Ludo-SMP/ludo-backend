FROM amazoncorretto:17

WORKDIR /app

COPY build/libs/*-SNAPSHOT.jar app.jar

ENV DB_URL=${DB_URL}
ENV DB_NAME=${DB_NAME}
ENV DB_PASSWORD=${DB_PASSWORD}

EXPOSE 8080

ENTRYPOINT [ "java", "-Dspring.profiles.active=prod", "-jar", "app.jar" ]