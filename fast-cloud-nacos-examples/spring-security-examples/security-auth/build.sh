#!/usr/bin/env bash
docker rmi springcloudnacos/auth auth

mvn clean package -Dmaven.skip.test=true

docker build -t auth .

docker tag auth:latest springcloudnacos/auth

docker push springcloudnacos/auth

docker images | grep auth

