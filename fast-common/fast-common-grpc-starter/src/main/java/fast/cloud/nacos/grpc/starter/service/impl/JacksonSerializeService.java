package fast.cloud.nacos.grpc.starter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import fast.cloud.nacos.grpc.starter.GrpcService.Request;
import fast.cloud.nacos.grpc.starter.GrpcService.Response;
import fast.cloud.nacos.grpc.starter.service.GrpcRequest;
import fast.cloud.nacos.grpc.starter.service.GrpcResponse;
import fast.cloud.nacos.grpc.starter.service.SerializeService;
import java.io.IOException;

/**
 * @author qinfuxiang
 * @Date 2020/6/12 9:24
 */
public class JacksonSerializeService implements SerializeService {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    @Override
    public ByteString serialize(GrpcResponse response) {
        try {
            return ByteString.copyFrom(getInstance().writeValueAsBytes(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ByteString serialize(GrpcRequest request) {
        try {
            return ByteString.copyFrom(getInstance().writeValueAsBytes(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public GrpcRequest deserialize(Request request) {
        byte[] bytes = request.getRequest().toByteArray();
        try {
            return getInstance().readValue(bytes, GrpcRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public GrpcResponse deserialize(Response response) {
        byte[] bytes = response.getResponse().toByteArray();
        try {
            return getInstance().readValue(bytes, GrpcResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
