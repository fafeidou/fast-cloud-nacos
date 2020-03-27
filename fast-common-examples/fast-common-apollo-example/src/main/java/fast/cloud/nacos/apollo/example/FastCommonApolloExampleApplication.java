package fast.cloud.nacos.apollo.example;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableApolloConfig
public class FastCommonApolloExampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonApolloExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Config config = ConfigService.getAppConfig();
        String someKey = "sms.enable";
        while (true) {
            String value = config.getProperty(someKey, null);
            System.out.printf("now: %s, sms.enable: %s%n", LocalDateTime.now().toString(),
                    value);
            Thread.sleep(3000L);
        }
    }
}
