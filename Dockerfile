ARG APP_NAME
ARG PORT

FROM node:20-alpine AS frontend-builder
WORKDIR /app
COPY package*.json ./
COPY tsconfig*.json ./
COPY ./client ./client
COPY ./libs ./libs

RUN npm install
RUN echo "Building libs"
RUN npm run build:libs
RUN echo "Building ${APP_NAME}"
RUN npm run build --workspace="@react-mfe-federation/${APP_NAME}"

FROM eclipse-temurin:21-jdk-alpine AS backend-builder
ARG APP_NAME
WORKDIR /app/server/${APP_NAME}
# Copy the specific app's backend files
COPY ./server/${APP_NAME}/pom.xml ./pom.xml
COPY ./server/${APP_NAME}/mvnw ./mvnw
COPY ./server/${APP_NAME}/.mvn ./.mvn
COPY ./server/${APP_NAME}/src ./src

# Create the static directory
RUN mkdir -p src/main/resources/static

# Copy the entire dist folder content to static
COPY --from=frontend-builder /app/client/${APP_NAME}/dist/ src/main/resources/static

# Make mvnw executable and run Maven build
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
ARG APP_NAME
ARG PORT
WORKDIR /app
COPY --from=backend-builder /app/server/${APP_NAME}/target/*.jar app.jar

ENV SERVER_PORT=${PORT}
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]