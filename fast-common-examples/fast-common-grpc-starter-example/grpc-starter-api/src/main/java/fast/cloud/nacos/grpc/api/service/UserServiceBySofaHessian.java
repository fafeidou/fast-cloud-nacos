package fast.cloud.nacos.grpc.api.service;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import java.util.List;

@GrpcService(server = "user",grpcServer = "user-grpc")
public interface UserServiceBySofaHessian {

    void insert(UserEntity userEntity);

    void deleteById(Long id);

    List<UserEntity> findAll();

}
