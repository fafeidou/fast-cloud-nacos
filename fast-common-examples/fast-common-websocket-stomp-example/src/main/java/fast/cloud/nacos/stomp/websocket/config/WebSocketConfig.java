package fast.cloud.nacos.stomp.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author qinfuxiang
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(StompProperties.class)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private StompProperties stompProperties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理  Message Broker 点对点式
        registry.setApplicationDestinationPrefixes("/app") // 配置请求都以/app打头，没有特殊意义，例如：@MessageMapping("/hello")，其实真实路径是/app/hello
                .enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(stompProperties.getServer())
                .setRelayPort(stompProperties.getPort())
                .setClientLogin(stompProperties.getUsername())
                .setClientPasscode(stompProperties.getPassword())
                .setSystemLogin(stompProperties.getUsername())
                .setSystemPasscode(stompProperties.getPassword());
//                .setVirtualHost("/")
//                .setUserDestinationBroadcast("/topic/greetings")
//                .setUserRegistryBroadcast("/topic/greetings");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();//注册STOMP协议的节点 指定使用SockJS协议,setAllowedOrigins 添加允许跨域访问
    }
}
