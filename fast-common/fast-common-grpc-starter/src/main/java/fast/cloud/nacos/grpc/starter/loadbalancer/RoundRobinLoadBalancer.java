package fast.cloud.nacos.grpc.starter.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer<T> implements LoadBalancer<T> {
    private AtomicInteger nextServerCyclicCounter;

    public RoundRobinLoadBalancer() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    /**
     * Please refer to com.netflix.loadbalancer.RoundRobinRule#choose(ILoadBalancer lb, Object key)
     *
     * @param serviceInstances
     * @return
     */
    @Override
    public T doSelect(Collection<T> serviceInstances) {
        int nextServerIndex = incrementAndGetModulo(serviceInstances.size());
        ArrayList<T> instances = (ArrayList<T>) serviceInstances;
        return instances.get(nextServerIndex);
    }

    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }

}
