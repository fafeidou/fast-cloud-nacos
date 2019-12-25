#!/usr/bin/env bash
docker rmi springcloudnacos/security-provider security-provider

mvn clean package -Dmaven.skip.test=true

docker build -t security-provider .

docker tag security-provider:latest springcloudnacos/security-provider

docker push springcloudnacos/security-provider

docker images | grep security-provider

