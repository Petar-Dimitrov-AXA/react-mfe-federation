# Use a multi-stage build to minimize the final image size
# Stage 1: Build the frontend
FROM node:16 AS frontend-builder
ARG APP_NAME
WORKDIR /app
COPY ./apps/${APP_NAME}/ .
RUN npm install && npm run build

# Stage 2: Build the backend
FROM maven:3.9.9-eclipse-temurin-21 AS backend-builder
ARG APP_NAME
WORKDIR /app
COPY ./server/${APP_NAME}/ .
COPY --from=frontend-builder /app/dist /app/src/main/resources/static
RUN mvn clean package -DskipTests

# Stage 3: Create the final image
FROM eclipse-temurin:21-jre
ARG APP_NAME
WORKDIR /app
COPY --from=backend-builder /app/target/${APP_NAME}-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]