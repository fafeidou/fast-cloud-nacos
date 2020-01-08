package fast.cloud.nacos.grpc.example.config;

import fast.cloud.nacos.grpc.example.grpc.GrpcNacosOptions;
import fast.cloud.nacos.grpc.example.grpc.GrpcNacosProto;
import fast.cloud.nacos.grpc.example.internal.NacosNameResolverProvider;
import io.grpc.Attributes;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GrpcEurekaConfiguration {

    @Bean
    @ConditionalOnMissingBean(ManagedChannel.class)
    public ManagedChannel managedChannel() {
        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
        return ManagedChannelBuilder.forTarget("nacos://" + "demo")
                .nameResolverFactory(new NacosNameResolverProvider(uri, Attributes.newBuilder().build()))
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();
    }

}
