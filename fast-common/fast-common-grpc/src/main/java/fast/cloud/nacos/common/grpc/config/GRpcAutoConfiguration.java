package fast.cloud.nacos.common.grpc.config;

import fast.cloud.nacos.common.grpc.runner.GRpcServerRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GRpcServerProperties.class)
public class GRpcAutoConfiguration {
    @Bean
    public GRpcServerRunner grpcServerRunner() {
        return new GRpcServerRunner();
    }
}
