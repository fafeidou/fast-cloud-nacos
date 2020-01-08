package fast.cloud.nacos.grpc.example;

import com.google.common.util.concurrent.ListenableFuture;
import fast.cloud.nacos.grpc.example.grpc.*;
import fast.cloud.nacos.grpc.example.internal.NacosNameResolverProvider;
import io.grpc.Attributes;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class HelloWorldClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ManagedChannel channel;
    private final GrpcTestServiceGrpc.GrpcTestServiceBlockingStub blockingStub;
    private final GrpcTestServiceGrpc.GrpcTestServiceStub grpcTestServiceStub;

    public HelloWorldClient(URI uri, String nacosServiceId) {
        this(ManagedChannelBuilder.forTarget("nacos://" + nacosServiceId)
                .nameResolverFactory(new NacosNameResolverProvider(uri, Attributes.newBuilder().build()))
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build());
    }

    public HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GrpcTestServiceGrpc.newBlockingStub(channel);
        grpcTestServiceStub = GrpcTestServiceGrpc.newStub(channel);
    }

    public void reqString(String req) {

        GrpcTestService_Response_String response = blockingStub.reqString(
                GrpcTestService_Request_String
                        .newBuilder()
                        .setName(req)
                        .build());
        System.out.println(response);
        //Async
        grpcTestServiceStub.reqString(GrpcTestService_Request_String
                .newBuilder()
                .setName("bb")
                .build(), new StreamObserver<GrpcTestService_Response_String>() {
            @Override
            public void onNext(GrpcTestService_Response_String grpcTestService_response_string) {
                System.out.println(grpcTestService_response_string);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
        //Future
        GrpcTestServiceGrpc.GrpcTestServiceFutureStub grpcTestServiceFutureStub = GrpcTestServiceGrpc.newFutureStub(channel);
        ListenableFuture<GrpcTestService_Response_String> response_stringListenableFuture = grpcTestServiceFutureStub.reqString(GrpcTestService_Request_String
                .newBuilder()
                .setName("dd")
                .build());
        try {
            GrpcTestService_Response_String grpcTestService_response_string = response_stringListenableFuture.get();
            System.out.println(grpcTestService_response_string);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
        HelloWorldClient client = new HelloWorldClient(uri, "demo");
        Thread.sleep(3000);
//        for (int i = 0; i < 100; i++) {
        client.reqString("AAA");
//        }


    }
}
