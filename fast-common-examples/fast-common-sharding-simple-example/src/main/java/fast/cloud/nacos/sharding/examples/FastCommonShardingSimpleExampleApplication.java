package fast.cloud.nacos.sharding.examples;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = SpringBootConfiguration.class)
public class FastCommonShardingSimpleExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonShardingSimpleExampleApplication.class, args);
    }

}
