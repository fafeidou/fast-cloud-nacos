package fast.cloud.nacos.grpc.example.service.client;

import fast.cloud.nacos.grpc.example.HelloWorldClient;
import fast.cloud.nacos.grpc.example.grpc.GrpcNacosOptions;
import fast.cloud.nacos.grpc.example.grpc.GrpcNacosProto;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Request_String;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Response_String;
import fast.cloud.nacos.grpc.example.service.GrpcTestServiceImpl;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class HelloService {
    @Autowired
    GrpcTestServiceImpl grpcTestService;

    public void hello() {
//        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
//        HelloWorldClient client = new HelloWorldClient(uri, "GrpcTestService");
//        client.reqString("AAA");
        grpcTestService.reqString(GrpcTestService_Request_String
                .newBuilder()
                .setName("BBB")
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
    }
}
