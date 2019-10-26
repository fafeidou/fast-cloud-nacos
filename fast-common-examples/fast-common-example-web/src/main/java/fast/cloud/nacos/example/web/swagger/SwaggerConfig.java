package fast.cloud.nacos.example.web.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Batman.qin
 * @Create 2018-11-30 14:42
 */
@Component
@ConfigurationProperties("batman.swagger")
public class SwaggerConfig {
    private List<Module> modules = new ArrayList<>();
    private String title;
    private String description;
    private String version;

    public SwaggerConfig() {
        super();
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
