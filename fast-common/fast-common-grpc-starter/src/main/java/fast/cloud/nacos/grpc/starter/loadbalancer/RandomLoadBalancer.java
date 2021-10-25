package fast.cloud.nacos.grpc.starter.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RandomLoadBalancer<T> implements LoadBalancer<T> {

    @Override
    public T doSelect(Collection<T> serviceInstances) {
        ArrayList<T> instances = (ArrayList<T>) serviceInstances;
        Collections.shuffle(instances);
        return instances.get(0);
    }
}

