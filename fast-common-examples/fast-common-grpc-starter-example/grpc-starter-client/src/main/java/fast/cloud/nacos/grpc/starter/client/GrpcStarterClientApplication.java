package fast.cloud.nacos.grpc.starter.client;

import fast.cloud.nacos.grpc.starter.annotation.GrpcServiceScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos"})
@GrpcServiceScan(packages = {"fast.cloud.nacos.grpc.api"})
public class GrpcStarterClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcStarterClientApplication.class, args);
    }

}
