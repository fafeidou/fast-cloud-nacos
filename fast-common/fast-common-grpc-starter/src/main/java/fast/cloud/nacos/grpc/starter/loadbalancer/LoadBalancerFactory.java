package fast.cloud.nacos.grpc.starter.loadbalancer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LoadBalancerFactory {
    private static Logger logger = LoggerFactory.getLogger(LoadBalancerFactory.class);

    private static final Map<String, LoadBalancer> loadBalancers = new HashMap<>();

    static {
        loadBalancers.put(LoadBalancer.RANDOM, new RandomLoadBalancer());
        loadBalancers.put(LoadBalancer.ROUND_ROBIN, new RoundRobinLoadBalancer());
    }

    /**
     * Static method to retrieve LB instance by given strategy. If no such strategy is not supported, fallback to the
     * backup one.
     *
     * @param strategy
     * @param backupStrategy
     * @return
     */
    public static LoadBalancer getLoadBalancer(String strategy, String backupStrategy) {
        if (StringUtils.isEmpty(strategy)) {
            throw new IllegalArgumentException("loadBalancer strategy must not be empty!");
        }

        if (!loadBalancers.containsKey(strategy)) {
            logger.warn("Load balancer strategy '{}' is not supported yet, fallback to backup '{}' strategy.", strategy, backupStrategy);
            strategy = backupStrategy;
        }
        return loadBalancers.get(strategy);
    }
}
