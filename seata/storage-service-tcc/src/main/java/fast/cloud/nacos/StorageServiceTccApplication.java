package fast.cloud.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos"}, exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class StorageServiceTccApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceTccApplication.class, args);
    }

}
