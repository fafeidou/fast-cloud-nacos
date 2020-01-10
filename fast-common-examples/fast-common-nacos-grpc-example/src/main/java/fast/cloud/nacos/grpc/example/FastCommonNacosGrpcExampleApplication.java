package fast.cloud.nacos.grpc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.common","fast.cloud.nacos.grpc.example"})
public class FastCommonNacosGrpcExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonNacosGrpcExampleApplication.class, args);
    }

}
