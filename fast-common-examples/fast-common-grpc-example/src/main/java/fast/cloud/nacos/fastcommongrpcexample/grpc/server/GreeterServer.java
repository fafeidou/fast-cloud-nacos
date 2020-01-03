package fast.cloud.nacos.fastcommongrpcexample.grpc.server;

import fast.cloud.nacos.fastcommongrpcexample.grpc.GreeterGrpc;
import fast.cloud.nacos.fastcommongrpcexample.grpc.Helloworld;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GreeterServer extends GreeterGrpc.GreeterImplBase implements InitializingBean {
    @Value("${gRPC.port}")
    private int port;

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerBuilder.forPort(port)
                .addService(new GreeterServer())
                .build()
                .start();
    }

    @Override
    public void sayHello(Helloworld.HelloRequest request,
                         io.grpc.stub.StreamObserver<Helloworld.HelloReply> responseObserver) {
        Helloworld.HelloReply result = Helloworld.HelloReply.newBuilder().setMessage(request.getName()).build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}
