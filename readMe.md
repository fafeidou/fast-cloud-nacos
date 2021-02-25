# fast-cloud-nacos

## 前言

> `fast-cloud-nacos`项目致力于打造一个基于nacos为注册中心，结合企业开发习惯，总结的一些基本的实现方式。

### 组织结构

``` ssh
fast-cloud-nacos
|---------------fast-boot-examples                   springboot原理的介绍
       |---------------auto-configure                自动化配置
       |---------------spring-boot-servlet servlet   同步异步实现
       |---------------spring-webflux                webflux的crud
       |---------------spring-webflux-client         远程调用webflux 
       |---------------spring-webmvc                 xml配置webmvc 
|---------------fast-cloud-nacos-examples
       |---------------cloud-stream-examples
                |---------------stream-kafka-consumer   stream kafka 
                |---------------stream-kafka-provider   stream kafka 
                |---------------stream-rabbit-consumer  stream rabbit
                |---------------stream-rabbit-provider  stream rabbit
                |---------------stream-rocket-consumer  stream rocket
                |---------------stream-rocket-provider  stream rocket
       |---------------cloud-rpc-examples            包括微服务调用怎么记录traceId和spanId
                |---------------api-gateway          spring cloud gateway 网关
                |---------------gateway-limiter      gateway实现限流
                |---------------nacos-zipkin         zipkin链路追踪
                |---------------open-api             feign接口
                |---------------service-feign        服务消费方
                |---------------service-file         feign实现文件上传
                |---------------service-hi           服务提供方
       |---------------dubbo-examples
                |---------------fast-dubbo-api                   通用接口
                |---------------fast-dubbo-client dubbo          消费方
                |---------------fast-dubbo-cloud-consumer        feign消费方
                |---------------fast-dubbo-server                dubbo提供方
       |---------------sentinel-examples
                |---------------service-consumer-fallback-sentinel 熔断测试
                |---------------service-consumer-sentinel          消费方整合sentinel
                |---------------service-gateway-sentinel           网关整合sentinel
                |---------------service-provider-sentinel          提供方整合sentinel
                |---------------service-sentinel-nacos             sentinel规则存储到nacos
       |---------------spring-security-examples
                |---------------security-api                       通用feign接口
                |---------------security-api-gateway               auth2整合网关
                |---------------security-auth                      auth2认证服务
                |---------------security-consumer                  服务消费方，集成security，并且实现feign的token传递
                |---------------security-provider                  实现jwt认证，非对称加密
|---------------fast-common
       |---------------fast-common-es         es通用 TODO
       |---------------fast-common-model      工程公用的model
       |---------------fast-common-mybatis    mybatis-plus 整理到接口
       |---------------fast-common-grpc       grpc端口注册到nacos并且实现负载均衡
       |---------------fast-common-grpc-starter       grpc整合spring，抽取通用的protobuf文件
       |---------------fast-common-custom-mybatis  自定义mybatis
       |---------------fast-common-tenant          通用多租户底层实现
       |---------------fast-common-juc          juc的一些demo
|---------------fast-common-examples
       |---------------fast-commmon-prometheus-example                k8s部署pro、grafana监控springboot
       |---------------fast-common-apollo-example                     apollo学习
       |---------------fast-common-es-example
                |---------------fast-common-es-jest-client-example    jest es客户端
                |---------------fast-common-es-jpa-example            jap es客户端
                |---------------fast-common-es-rest-client-example    rest es 客户端
       |---------------fast-common-example-web                        做国际化和swagger统一配置、异常处理
       |---------------fast-common-grpc-example                       grpc 提供客户端服务端
       |---------------fast-common-grpc-proto                         grpc idl proto生成
       |---------------fast-common-grpc-starter-example               grpc 整合springboot 
                |---------------grpc-starter-api                      grpc api接口抽取
                |---------------grpc-starter-client                   grpc 消费方
                |---------------grpc-starter-provider                 grpc 服务提供方
       |---------------fast-common-jvm-example                        jvm的案例
       |---------------fast-common-nacos-grpc-example                 nacos整合grpc负载均衡
       |---------------fast-common-rocketmq-example                   rocketmq测试样例
       |---------------fast-common-sharding-horizontal-subdivision    sharding jdbc 水平分库分表
       |---------------fast-common-sharding-rw-separation             sharding jdbc 主从读写分离
       |---------------fast-common-sharding-simple-example            sharding jdbc 水平分表
       |---------------fast-common-sharding-vertial-subdivision       sharding jdbc 垂直分库及公共表
       |---------------fast-common-tenant-example                     多租户常见案例
       |---------------fast-common-webflux-client                     webflux的通用客户端
       |---------------fast-common-websocket-simple-example           简单的websocket的demo
       |---------------fast-common-websocket-stomp-example            通过stomp实现的websocket
       |---------------fast-elk-example                               springboot kafka日志发送到elk
       |---------------fast-rocketmq-tx                               rocketmq实现分布式事务
                |---------------rocketmq-tx-bank1                     可靠性消息实现分布式事务demo1
                |---------------rocketmq-tx-bank2                     可靠性消息实现分布式事务demo2
|---------------fast-source-code-analysis
       |---------------code-mybatis                                   mybatis源码分析以及测试mybatis的自定义
       |---------------code-spring                                    spring源码分析专用
```
