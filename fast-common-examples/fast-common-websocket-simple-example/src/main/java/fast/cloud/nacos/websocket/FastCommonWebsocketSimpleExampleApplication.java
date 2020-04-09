package fast.cloud.nacos.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qinfuxiang
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FastCommonWebsocketSimpleExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonWebsocketSimpleExampleApplication.class, args);
    }

}
