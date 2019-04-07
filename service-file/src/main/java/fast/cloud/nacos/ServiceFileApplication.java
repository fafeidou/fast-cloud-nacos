package fast.cloud.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFileApplication.class, args);
    }

}
