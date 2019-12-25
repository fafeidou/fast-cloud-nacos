FROM registry.cn-hangzhou.aliyuncs.com/micro-java/openjdk:8-jre-alpine

MAINTAINER batman@163.com

ADD target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT [ "java","-jar","/app.jar" ]
