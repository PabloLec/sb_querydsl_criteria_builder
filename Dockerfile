FROM eclipse-temurin:21-jdk-alpine AS builder

RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .
COPY querydsl-criteria-builder ./querydsl-criteria-builder
COPY test-backend ./test-backend

RUN --mount=type=cache,target=/root/.m2/repository mvn dependency:go-offline

RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/test-backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
