cd server

mvn clean install

cd target/

java -jar ./SprintOAuth2-0.0.1-SNAPSHOT.war --spring.profiles.active=dev


