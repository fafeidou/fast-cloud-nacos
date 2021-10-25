package fast.cloud.nacos.cat.monitor.config;


import fast.cloud.nacos.cat.monitor.util.HostUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

@Configuration
public class MonitorEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(MonitorEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {

            String env = environment.getProperty("spring.profiles.active","default");

            String appId = environment.getProperty("spring.application.name","default");

            String ip = HostUtil.getHostIp();
            logger.info("init the common tags ip={},appid={},env={}", ip, appId, env);
            System.setProperty("spring.devtools.restart.enabled", "false");
            Properties properties = new Properties();
            properties.put("management.endpoints.web.exposure.include", "info,loggers,metrics,threaddump,prometheus,health,env,configprops,version,unregister,register");
            properties.put("management.health.*.enabled", "false");
            properties.put("management.health.defaults.enabled", "false");
            properties.put("management.health.ping.enabled", "true");
            properties.put("spring.devtools.restart.enabled", "false");
            PropertiesPropertySource propertySource = new PropertiesPropertySource("management", properties);
            environment.getPropertySources().addLast(propertySource);
        } catch (Exception e) {
            logger.error("Process monitor environment error", e);
        }

    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name:default}") String applicationName ,@Value("${spring.profiles.active:default}") String env) {
        String ip = HostUtil.getHostIp();
        return (registry) -> registry.config().commonTags("application", applicationName).commonTags("env",env).commonTags("ip",ip);
    }
}

