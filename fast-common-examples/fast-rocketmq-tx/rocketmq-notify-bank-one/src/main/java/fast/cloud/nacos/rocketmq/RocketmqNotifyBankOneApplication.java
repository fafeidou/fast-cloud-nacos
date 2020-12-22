package fast.cloud.nacos.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableHystrix
@EnableFeignClients(basePackages = {"fast.cloud.nacos.rocketmq.spring"})
public class RocketmqNotifyBankOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqNotifyBankOneApplication.class, args);
    }

}
