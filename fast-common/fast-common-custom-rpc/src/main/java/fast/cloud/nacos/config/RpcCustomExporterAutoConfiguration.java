package fast.cloud.nacos.config;

import fast.cloud.nacos.handler.AutoDemoServiceImplExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcCustomExporterAutoConfiguration {
    @Bean
    public static AutoDemoServiceImplExporter autoJsonRpcServiceImplExporter() {
        return new AutoDemoServiceImplExporter();
    }
}
