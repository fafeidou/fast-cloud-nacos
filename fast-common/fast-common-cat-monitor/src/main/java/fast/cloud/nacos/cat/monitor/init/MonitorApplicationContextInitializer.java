package fast.cloud.nacos.cat.monitor.init;


import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 20)
@Component
public class MonitorApplicationContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(MonitorApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        if (!environment.getPropertySources().contains("bootstrap")) {
            if (Boolean.parseBoolean(environment.getProperty("monitor.enable", "true"))) {

                String appId = environment.getProperty("app.id");

                Cat.initializeByDomain(appId, "192.168.56.101:2280");
            }
        }
    }
}
