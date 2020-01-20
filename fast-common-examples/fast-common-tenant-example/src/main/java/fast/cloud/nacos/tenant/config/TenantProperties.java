package fast.cloud.nacos.tenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("tenant")
public class TenantProperties {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
