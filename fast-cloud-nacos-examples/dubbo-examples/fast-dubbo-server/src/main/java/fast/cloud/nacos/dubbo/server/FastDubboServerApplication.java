package fast.cloud.nacos.dubbo.server;

import com.alibaba.dubbo.config.annotation.Reference;
import fast.cloud.nacos.dubbo.api.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FastDubboServerApplication {
    @Reference
    HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(FastDubboServerApplication.class, args);
    }

}
