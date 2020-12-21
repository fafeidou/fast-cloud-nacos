package fast.cloud.nacos.apollo.sentinel;

import com.ctrip.framework.apollo.ConfigService;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ServiceGatewayApolloSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayApolloSentinelApplication.class, args);
        log.info("apollo properties start");
        Set<String> propertyNames = ConfigService.getAppConfig().getPropertyNames();
        for (String propertyName : propertyNames) {
            log.info("{} = {}", propertyName, ConfigService.getAppConfig().getProperty(propertyName, "无配置"));
        }
        log.info("apollo properties end");
    }

}
