package fast.cloud.nacos.prometheus.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fast.cloud.nacos")
public class FastCommmonPrometheusExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommmonPrometheusExampleApplication.class, args);
    }

}
