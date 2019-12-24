package fast.cloud.nacos.securityauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.common.model","fast.cloud.nacos.securityauth"})
public class SecurityAuthApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthApplication.class, args);
    }

}
