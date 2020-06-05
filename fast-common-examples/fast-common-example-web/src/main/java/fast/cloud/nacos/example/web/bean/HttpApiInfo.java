package fast.cloud.nacos.example.web.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:46
 */
public class HttpApiInfo {
    @JsonProperty("auth_item")
    private String authItem;
    private String name;

    public HttpApiInfo() {
    }

    public String getAuthItem() {
        return this.authItem;
    }

    public void setAuthItem(String authItem) {
        this.authItem = authItem;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
