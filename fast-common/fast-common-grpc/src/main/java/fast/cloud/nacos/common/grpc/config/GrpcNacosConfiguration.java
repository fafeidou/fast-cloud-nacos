package fast.cloud.nacos.common.grpc.config;

import fast.cloud.nacos.common.grpc.internal.NacosNameResolverProvider;
import io.grpc.Attributes;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GrpcNacosConfiguration {
    @Value("${nacos.addr}")
    private String serverAddr;

    @Bean
    @ConditionalOnMissingBean(ManagedChannel.class)
    public ManagedChannel managedChannel() {
        URI uri = URI.create(serverAddr);
        return ManagedChannelBuilder.forTarget("nacos://" + "demo")
                .nameResolverFactory(new NacosNameResolverProvider(uri, Attributes.newBuilder().build()))
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();
    }

}
