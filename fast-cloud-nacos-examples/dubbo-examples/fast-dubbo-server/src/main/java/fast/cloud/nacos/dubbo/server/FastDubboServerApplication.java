package fast.cloud.nacos.dubbo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FastDubboServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastDubboServerApplication.class, args);
    }

}
