package fast.cloud.nacos.grpc.starter.router;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import fast.cloud.nacos.grpc.starter.loadbalancer.LoadBalancer;
import fast.cloud.nacos.grpc.starter.loadbalancer.LoadBalancerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class DynamicServiceSelector {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private NamingService namingService;

    @Autowired
    private NacosDiscoveryProperties properties;

    static final Map<String, List<ServiceInstance>> cachedServerList = new ConcurrentHashMap<>(16);

    ConcurrentMap<String, WatcherPhase> watcherPhaseMapping = new ConcurrentHashMap<>(16);

    enum WatcherPhase {
        Initializing,
        Initialized
    }

    public ServiceInstance getNextServer(String serviceId) {
        List<ServiceInstance> instances;
        if (watcherPhaseMapping.putIfAbsent(serviceId, WatcherPhase.Initializing) == null) {
            instances = discoveryClient.getInstances(serviceId);
            cachedServerList.put(serviceId, instances);
            try {
                namingService.subscribe(serviceId, Arrays.asList(properties.getClusterName()), event -> {
                    if (event instanceof NamingEvent) {
                        log.info("Nacos service discovery detected a service change event from Agent for serviceName '{}'.", serviceId);
                        cachedServerList.put(serviceId, discoveryClient.getInstances(serviceId));
                    }
                });
            } catch (NacosException e) {
                log.error("nacos event exception:{}",e);
            }
            watcherPhaseMapping.put(serviceId, WatcherPhase.Initialized);
        }
        return (ServiceInstance) LoadBalancerFactory
                .getLoadBalancer(LoadBalancer.ROUND_ROBIN, LoadBalancer.ROUND_ROBIN)
                .doSelect(cachedServerList.get(serviceId));
    }

}
