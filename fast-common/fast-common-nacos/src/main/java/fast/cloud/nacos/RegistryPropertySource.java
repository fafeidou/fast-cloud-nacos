package fast.cloud.nacos;

import org.springframework.core.env.PropertySource;

import java.util.HashMap;

public class RegistryPropertySource extends PropertySource {
    public static final String NAME = "spring.cloud.zk.service-registry.property-source";
    public static final String ENABLED = "spring.cloud.zk.service-registry.auto-registration.enabled";
    public static final String SPRING_CLOUD_AUTO_REGISTRYATION_ENABLED = "spring.cloud.service-registry.auto-registration.enabled";
    private HashMap<String, Object> properties = new HashMap();

    public RegistryPropertySource() {
        super("spring.cloud.nacos.service-registry.property-source");
        this.initProperties();
    }

    private void initProperties() {
        this.properties.put("spring.cloud.service-registry.auto-registration.enabled", "true");
    }

    @Override
    public Object getProperty(String name) {
        return this.properties.get(name);
    }
}