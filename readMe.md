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
       |---------------cloud-bus-examples
                |---------------bus-kafka-consumer   stream kafka TODO
                |---------------bus-kafka-provider   stream kafka TODO
                |---------------bus-rabbit-consumer  stream rabbit
                |---------------bus-rabbit-provider  stream rabbit
                |---------------bus-rocket-consumer  stream rocket
                |---------------bus-rocket-provider  stream rocket
       |---------------cloud-rpc-examples
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
|---------------fast-common-examples
       |---------------fast-common-es-example
                |---------------fast-common-es-jest-client-example    jest es客户端
                |---------------fast-common-es-jpa-example            jap es客户端
                |---------------fast-common-es-rest-client-example    rest es 客户端
       |---------------fast-common-example-web                        做国际化和swagger统一配置、异常处理
       |---------------fast-common-grpc-example                       grpc 提供客户端服务端
       |---------------fast-common-grpc-proto                         grpc idl proto生成
       |---------------fast-common-jvm-example                        jvm的案例
       |---------------fast-common-webflux-client                     webflux的通用客户端
       |---------------fast-common-nacos-grpc-example                 nacos整合grpc负载均衡
|---------------fast-source-code-analysis
       |---------------code-mybatis                                   mybatis源码分析
```
