package fast.cloud.nacos.fastcommonnacosgrpcexample.service;

import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcTestServiceGrpc;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcTestService_Request_String;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcTestService_Response_String;
import io.grpc.stub.StreamObserver;

public class GrpcTestServiceImpl extends GrpcTestServiceGrpc.GrpcTestServiceImplBase {

    @Override
    public void reqString(GrpcTestService_Request_String request,
                          StreamObserver<GrpcTestService_Response_String> responseObserver) {
        String name = request.getName();
        responseObserver.onNext(GrpcTestService_Response_String.newBuilder().setResult("success:" + name).build());
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