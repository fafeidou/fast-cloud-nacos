#!/usr/bin/env bash
docker rmi springcloudnacos/api-gateway api-gateway

mvn clean package -Dmaven.skip.test=true

docker build -t api-gateway .

docker tag api-gateway:latest springcloudnacos/api-gateway

docker push springcloudnacos/api-gateway

docker images | grep api-gateway

