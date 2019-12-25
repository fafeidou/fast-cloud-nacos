package fast.cloud.nacos.securityconsumer;

import fast.cloud.nacos.securityconsumer.interceptor.FeignClientInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients(basePackages = "fast.cloud.nacos.securityapi")
@SpringBootApplication
public class SecurityConsumerApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FeignClientInterceptor feignClientInterceptor() {
        return new FeignClientInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityConsumerApplication.class, args);
    }

}
