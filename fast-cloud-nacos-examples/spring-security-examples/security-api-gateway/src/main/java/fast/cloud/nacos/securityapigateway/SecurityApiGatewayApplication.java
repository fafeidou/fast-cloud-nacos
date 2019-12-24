package fast.cloud.nacos.securityapigateway;

import fast.cloud.nacos.securityapigateway.filter.TokenGlobalFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.securityapigateway", "fast.cloud.nacos.common.model"})
public class SecurityApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApiGatewayApplication.class, args);
    }

    @Bean
    public TokenGlobalFilter tokenGlobalFilter() {
        return new TokenGlobalFilter();
    }
}
