package fast.cloud.nacos.grpc.example.service;

import fast.cloud.nacos.grpc.example.annoation.GRpcService;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestServiceGrpc;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Request_String;
import fast.cloud.nacos.grpc.example.grpc.GrpcTestService_Response_String;
import io.grpc.stub.StreamObserver;

@GRpcService
public class GrpcTestServiceImpl extends GrpcTestServiceGrpc.GrpcTestServiceImplBase {

    @Override
    public void reqString(GrpcTestService_Request_String request,
                          StreamObserver<GrpcTestService_Response_String> responseObserver) {
        String name = request.getName();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(GrpcTestService_Response_String.newBuilder().setResult("success:2" + name).build());
        responseObserver.onCompleted();
    }

    @Override
    public void reqStringServerStream(GrpcTestService_Request_String request,
                                      StreamObserver<GrpcTestService_Response_String> responseObserver) {
        String name = request.getName();
        responseObserver.onNext(GrpcTestService_Response_String.newBuilder().setResult("success_1:" + name).build());
        responseObserver.onNext(GrpcTestService_Response_String.newBuilder().setResult("success_2:" + name).build());
        responseObserver.onNext(GrpcTestService_Response_String.newBuilder().setResult("success_3:" + name).build());
        responseObserver.onCompleted();
    }
}