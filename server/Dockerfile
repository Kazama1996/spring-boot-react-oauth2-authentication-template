FROM openjdk:17-oracle

WORKDIR /app

COPY target/SprintOAuth2-0.0.1-SNAPSHOT.war /app/app.war

EXPOSE 8080

CMD ["java" , "-jar" , "app.war" , "--spring.profiles.active=prod"]