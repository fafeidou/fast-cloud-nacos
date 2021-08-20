package fast.cloud.nacos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class RegistryPropertySourceContextInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(RegistryPropertySourceContextInitializer.class);

    public RegistryPropertySourceContextInitializer() {
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        String enabled = environment.getProperty("spring.cloud.nacos.service-registry.auto-registration.enabled", "true");
        log.info("{}={}", "spring.cloud.nacos.service-registry.auto-registration.enabled", enabled);
        if (Boolean.valueOf(enabled)) {
            if (!environment.getPropertySources().contains("spring.cloud.nacos.service-registry.property-source")) {
                environment.getPropertySources().addLast(new RegistryPropertySource());
            }
        }
    }
}
