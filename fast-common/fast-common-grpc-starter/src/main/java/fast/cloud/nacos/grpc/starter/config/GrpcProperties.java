package fast.cloud.nacos.grpc.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.grpc")
public class GrpcProperties {

    /**
     * enable server start
     */
    private boolean enable;

    /**
     * server listen port
     */
    private int port;

    private String grpcServerName;

    /**
     * client config
     */
    private List<RemoteServer> remoteServers;

    /**
     * client interceptor
     */
    private Class clientInterceptor;

    /**
     * server interceptor
     */
    private Class serverInterceptor;

}