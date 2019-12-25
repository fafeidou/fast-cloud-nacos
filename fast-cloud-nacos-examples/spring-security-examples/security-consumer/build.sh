#!/usr/bin/env bash
docker rmi springcloudnacos/security-consumer security-consumer -U

mvn clean package -Dmaven.skip.test=true

docker build -t security-consumer .

docker tag security-consumer:latest springcloudnacos/security-consumer

docker push springcloudnacos/security-consumer

docker images | grep security-consumer

