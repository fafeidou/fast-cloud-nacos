package fast.cloud.nacos.grpc.api.service;


import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import fast.cloud.nacos.grpc.starter.constant.SerializeType;
import java.util.List;

@GrpcService(server = "user", serialization = SerializeType.FASTJSON,grpcServer = "user-grpc")
public interface UserServiceByFastJSON {

    void insert(String userEntityJson);

    void deleteById(String id);

    List<UserEntity> findAll();

}
