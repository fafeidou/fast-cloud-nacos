package fast.cloud.nacos.grpc.starter;

import fast.cloud.nacos.ApplicationContextUtil;
import fast.cloud.nacos.grpc.starter.config.GrpcProperties;
import fast.cloud.nacos.grpc.starter.config.RemoteServer;
import fast.cloud.nacos.grpc.starter.router.DynamicServiceSelector;
import fast.cloud.nacos.grpc.starter.service.SerializeService;
import io.grpc.*;
import io.grpc.internal.DnsNameResolverProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcClient {

    private static final Map<String, ServerContext> serverMap = new HashMap<>();

    private final GrpcProperties grpcProperties;

    private final SerializeService serializeService;

    private ClientInterceptor clientInterceptor;

    public GrpcClient(GrpcProperties grpcProperties, SerializeService serializeService) {
        this.grpcProperties = grpcProperties;
        this.serializeService = serializeService;
    }

    public GrpcClient(GrpcProperties grpcProperties, SerializeService serializeService, ClientInterceptor clientInterceptor) {
        this.grpcProperties = grpcProperties;
        this.serializeService = serializeService;
        this.clientInterceptor = clientInterceptor;
    }

    /**
     * 初始化
     */
    public void init(){
        List<RemoteServer> remoteServers = grpcProperties.getRemoteServers();
        if (!CollectionUtils.isEmpty(remoteServers)) {
            for (RemoteServer server : remoteServers) {
                ManagedChannel channel = ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                        .defaultLoadBalancingPolicy("round_robin")
                        .nameResolverFactory(new DnsNameResolverProvider())
                        .idleTimeout(30, TimeUnit.SECONDS)
                        .usePlaintext().build();
                if (clientInterceptor != null){
                    Channel newChannel = ClientInterceptors.intercept(channel, clientInterceptor);
                    serverMap.put(server.getServer(), new ServerContext(newChannel, serializeService));
                }else {
                    Class clazz = grpcProperties.getClientInterceptor();
                    if (clazz == null) {
                        serverMap.put(server.getServer(), new ServerContext(channel, serializeService));
                    }else {
                        try {
                            ClientInterceptor interceptor = (ClientInterceptor) clazz.newInstance();
                            Channel newChannel = ClientInterceptors.intercept(channel, interceptor);
                            serverMap.put(server.getServer(), new ServerContext(newChannel, serializeService));
                        } catch (InstantiationException | IllegalAccessException e) {
                            log.warn("ClientInterceptor cannot use, ignoring...");
                            serverMap.put(server.getServer(), new ServerContext(channel, serializeService));
                        }
                    }
                }
            }
        }
    }

    /**
     * 连接远程服务
     */
    public static ServerContext connect(String serverName) {
        return serverMap.get(serverName);
    }

    public static ServerContext request(String serverName) {
        DynamicServiceSelector dynamicServiceSelector = ApplicationContextUtil.getBean(DynamicServiceSelector.class);
        ServiceInstance serviceInstance = dynamicServiceSelector.getNextServer(serverName);
        return request(serviceInstance.getHost(), serviceInstance.getPort());
    }


    public static ServerContext request(String host,Integer port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
//                .defaultLoadBalancingPolicy("round_robin")
//                .nameResolverFactory(new DnsNameResolverProvider())
                .idleTimeout(30, TimeUnit.SECONDS)
                .usePlaintext().build();
        SerializeService serializeService = ApplicationContextUtil.getBean(SerializeService.class);
        ServerContext serverContext = new ServerContext(channel, serializeService);
        return serverContext;
    }
}
