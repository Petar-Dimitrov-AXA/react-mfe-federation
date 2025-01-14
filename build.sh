#!/bin/bash

APP_NAME=$1
PORT=$2

if [ -z "$APP_NAME" ] || [ -z "$PORT" ]; then
    echo "Usage: ./build-app.sh <app-name> <port>"
    exit 1
fi

docker build \
    --build-arg APP_NAME=$APP_NAME \
    --build-arg PORT=$PORT \
    -t my-repo/$APP_NAME:latest \
    -f Dockerfile.template .