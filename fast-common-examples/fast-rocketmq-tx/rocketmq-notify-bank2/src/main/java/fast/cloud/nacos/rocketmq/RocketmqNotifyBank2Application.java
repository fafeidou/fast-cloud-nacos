package fast.cloud.nacos.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class RocketmqNotifyBank2Application {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqNotifyBank2Application.class, args);
    }

}
