package fast.cloud.nacos.grpc.starter.service;

import com.google.protobuf.ByteString;
import fast.cloud.nacos.grpc.starter.GrpcService;

public interface SerializeService {

    /**
     * 序列化
     */
    ByteString serialize(GrpcResponse response);

    /**
     * 序列化
     */
    ByteString serialize(GrpcRequest request);

    /**
     * 反序列化
     */
    GrpcRequest deserialize(GrpcService.Request request);

    /**
     * 反序列化
     */
    GrpcResponse deserialize(GrpcService.Response response);

}
