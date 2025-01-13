ARG APP_NAME
ARG PORT

FROM node:20-alpine AS frontend-builder
WORKDIR /app
COPY package*.json ./
COPY ./client ./client
COPY ./libs ./libs
COPY tsconfig.json .
#COPY vite.config.ts .
RUN npm install
RUN npm run build

FROM eclipse-temurin:21-jdk-alpine AS backend-builder
ARG APP_NAME
WORKDIR /app/server/${APP_NAME}
# Copy the specific app's backend files
COPY ./server/${APP_NAME}/pom.xml ./pom.xml
COPY ./server/${APP_NAME}/mvnw ./mvnw
COPY ./server/${APP_NAME}/.mvn ./.mvn
COPY ./server/${APP_NAME}/src ./src

# Make mvnw executable and run Maven build
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
ARG APP_NAME
ARG PORT
WORKDIR /app
# Copy the built JAR from the correct location
COPY --from=backend-builder /app/server/${APP_NAME}/target/*.jar app.jar
COPY --from=frontend-builder /app/client/${APP_NAME}/dist ./resources/static
ENV SERVER_PORT=${PORT}
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]