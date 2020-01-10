package fast.cloud.nacos.grpc.example.service.client;

import fast.cloud.nacos.grpc.example.grpc.GrpcTestServiceGrpc;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Request_String;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Response_String;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HelloService {


    @Autowired
    ManagedChannel managedChannel;

    private GrpcTestServiceGrpc.GrpcTestServiceBlockingStub blockingStub;

    public String hello(String name) {
        GrpcTestService_Response_String grpcTestService_response_string = blockingStub.reqString(GrpcTestService_Request_String
                .newBuilder()
                .setName(name)
                .build());

        return grpcTestService_response_string.getResult();
    }


    @PostConstruct
    private void initializeClient() {
        blockingStub = GrpcTestServiceGrpc.newBlockingStub(managedChannel);
    }
}
