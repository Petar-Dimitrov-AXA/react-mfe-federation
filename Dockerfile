ARG APP_NAME
ARG PORT

FROM node:18-alpine AS frontend-builder
WORKDIR /app
COPY package*.json ./
COPY ./client ./client
COPY ./libs ./libs
COPY tsconfig.json .
#COPY vite.config.ts .
RUN npm install
RUN npm run build

FROM eclipse-temurin:17-jdk-alpine AS backend-builder
ARG APP_NAME
WORKDIR /app/server/${APP_NAME}
# Copy the specific app's backend files
COPY ./server/${APP_NAME}/pom.xml ./pom.xml
COPY ./server/${APP_NAME}/mvnw ./mvnw
COPY ./server/${APP_NAME}/.mvn ./.mvn
COPY ./server/${APP_NAME}/src ./src

# Create the static directory
RUN mkdir -p src/main/resources/staic

# Copy the entire dist folder content to static
COPY --from=frontend-builder /app/client/${APP_NAME}/dist/ src/main/resources/static

# Make mvnw executable and run Maven build
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
ARG APP_NAME
ARG PORT
WORKDIR /app
COPY --from=backend-builder /app/server/${APP_NAME}/target/*.jar app.jar

ENV SERVER_PORT=${PORT}
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]