package fast.cloud.nacos.fastcommongrpcexample.grpc;

import fast.cloud.nacos.fastcommongrpcexample.io.grpc.LocalNameResolverProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;

import java.util.concurrent.TimeUnit;

public class SimpleClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public SimpleClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private SimpleClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder
                //添加负载均衡
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                //负载均衡策略
                .nameResolverFactory(new LocalNameResolverProvider())
                .build();
        greeterBlockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String myHello(String name) {
        Helloworld.HelloRequest request = Helloworld.HelloRequest.newBuilder().setName(name).build();
        Helloworld.HelloReply helloReply = greeterBlockingStub.sayHello(request);
        return helloReply.getMessage();
    }
}
