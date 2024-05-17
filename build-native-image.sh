mvn clean install -Pnative -DskipTests
cd test-backend
mvn spring-boot:build-image -Pnative -DskipTests