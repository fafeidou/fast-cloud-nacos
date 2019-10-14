package fast.cloud.nacos.servicehi;

import fast.cloud.nacos.servicehi.test.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHiApplication.class, args);
    }

    @Value(value = "${test.name:}")
    private String testName;

    @Bean
    @RefreshScope
    public Test test() {
        Test test = new Test();
        test.setName(testName);
        return test;
    }
}
