package fast.cloud.nacos.grpc.starter.loadbalancer;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.Collection;
import java.util.List;

public interface LoadBalancer<T> {
    String RANDOM = "Random";
    String ROUND_ROBIN = "RoundRobin";

    T doSelect(Collection<T> serviceInstances);

    default List<Server> getAllServers(ILoadBalancer ribbonLoadBalancer) {
        return ribbonLoadBalancer.getReachableServers();
    }

}