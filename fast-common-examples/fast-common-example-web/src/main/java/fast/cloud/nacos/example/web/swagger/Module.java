package fast.cloud.nacos.example.web.swagger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Batman.qin
 * @create 2018-12-07 20:13
 */
public class Module {
    private String moduleName;
    private List<String> packages = new ArrayList<>();
    private String groupName;

    public Module() {
        super();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
