
docker-compose down

docker rmi springoauth2template_spring-boot-app

cd server

mvn clean install

cd ..

docker-compose up -d
