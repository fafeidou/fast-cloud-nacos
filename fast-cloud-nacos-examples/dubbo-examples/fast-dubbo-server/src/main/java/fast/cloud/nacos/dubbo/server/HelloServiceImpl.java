package fast.cloud.nacos.dubbo.server;

import fast.cloud.nacos.dubbo.api.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 注意：是 org.apache.dubbo.config.annotation.Service 注解
@Service(version = "1.0.0")
@RestController
public class HelloServiceImpl implements HelloService {

    @Override
    @GetMapping("/test")
    public String hello(String name) {
        return "hello " + name;
    }

}