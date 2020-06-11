package fast.cloud.nacos.grpc.api.service;


import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import fast.cloud.nacos.grpc.starter.constant.SerializeType;
import java.util.List;

@GrpcService(server = "user", serialization = SerializeType.PROTOSTUFF)
public interface UserServiceByProtoStuff {

    void insert(UserEntity userEntity);

    void deleteById(Long id);

    List<UserEntity> findAll();

}
