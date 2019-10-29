package fast.cloud.nacos.dubbo.server;

import fast.cloud.nacos.dubbo.api.HelloService;
import org.apache.dubbo.config.annotation.Service;

// 注意：是 org.apache.dubbo.config.annotation.Service 注解
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }

}