package fast.cloud.nacos.apigateway;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.Executor;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConfigService configService = NacosFactory.createConfigService("localhost:8848");

        configService.addListener("test-gateway", "test-group", new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println(configInfo);
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }
}
