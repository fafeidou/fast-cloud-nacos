package fast.cloud.nacos.servicehi;

import com.dianping.cat.Cat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("fast.cloud.nacos")
//@ComponentScan(basePackages = {"com.batman", "fast.cloud.nacos.servicehi"})
public class ServiceHiApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        SpringApplication.run(ServiceHiApplication.class, args);
        Cat.initializeByDomain("service-hi", "192.168.56.101");
    }

}
