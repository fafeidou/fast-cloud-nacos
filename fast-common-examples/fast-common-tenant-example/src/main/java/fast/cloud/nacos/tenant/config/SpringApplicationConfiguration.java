package fast.cloud.nacos.tenant.config;

import fast.cloud.nacos.common.model.beans.context.SpringApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring相关应用配置
 */
@Configuration
public class SpringApplicationConfiguration {

    @Bean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }
}
