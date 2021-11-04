package fast.cloud.nacos.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos"})
//@GrpcServiceScan(packages = {"fast.cloud.nacos.grpc.api"})
public class GrpcStarterProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcStarterProviderApplication.class, args);
    }

}
