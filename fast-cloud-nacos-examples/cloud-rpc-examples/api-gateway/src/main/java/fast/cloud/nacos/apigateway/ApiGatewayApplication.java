package fast.cloud.nacos.apigateway;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.Executor;

@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class ApiGatewayApplication implements CommandLineRunner {
    // 日志前缀TAG，springboot标准（对应在配置中心的示例为：logging.level.com.shanhy.demo 其实就是对应的 com.shanhy.demo 这个包）
    private static final String LOGGER_TAG = "logging.level.";
    @Autowired
    private LoggingSystem loggingSystem;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConfigService configService = NacosFactory.createConfigService("localhost:8848");

        configService.addListener("test-gateway", "test-group", new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println(configInfo);
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        //动态修改日志级别
//        for (String key : changeEvent.changedKeys()) {
//            if (StringUtils.containsIgnoreCase(key, LOGGER_TAG)) {
//                ConfigChange change = changeEvent.getChange(key);
//                String strLevel = change.getNewValue() == null ? change.getOldValue() : change.getNewValue();
//                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
//                loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
//                log.info("{}={}", key, strLevel);
//            }
//        }
//        loggingSystem.setLogLevel();

    }
}
