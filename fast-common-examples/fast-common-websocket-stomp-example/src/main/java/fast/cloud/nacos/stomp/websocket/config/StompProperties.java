package fast.cloud.nacos.stomp.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qinfuxiang
 */
@ConfigurationProperties(prefix = "stomp", ignoreUnknownFields = false)
@Data
public class StompProperties {
    private String server;
    private Integer port;
    private String username;
    private String password;
}
