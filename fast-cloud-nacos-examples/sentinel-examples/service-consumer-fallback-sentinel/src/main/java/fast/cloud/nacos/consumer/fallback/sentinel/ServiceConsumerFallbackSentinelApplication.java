package fast.cloud.nacos.consumer.fallback.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import fast.cloud.nacos.consumer.fallback.sentinel.util.ExceptionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class ServiceConsumerFallbackSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumerFallbackSentinelApplication.class, args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
