package fast.cloud.nacos.fastcommongrpcexample.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class SimpleClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public SimpleClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private SimpleClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
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
