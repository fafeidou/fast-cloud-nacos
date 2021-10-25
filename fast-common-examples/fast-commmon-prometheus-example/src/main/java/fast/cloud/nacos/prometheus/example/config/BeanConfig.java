package fast.cloud.nacos.prometheus.example.config;

import fast.cloud.nacos.prometheus.example.endpoint.SimpleEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    @ConditionalOnEnabledEndpoint(endpoint = SimpleEndpoint.class)
    @ConditionalOnMissingBean
    public SimpleEndpoint simpleEndpoint() {
        return new SimpleEndpoint();
    }
}
