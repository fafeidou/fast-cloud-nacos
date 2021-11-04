package fast.cloud.nacos.provider.controller;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceBySofaHessian;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v0/user")
public class V0UserController {

    @GrpcService
    private UserServiceBySofaHessian userServiceBySofaHessian;

    @PostMapping("/add")
    public UserEntity insertUser(@RequestBody UserEntity userEntity) {
        userServiceBySofaHessian.insert(userEntity);
        return userEntity;
    }

    @GetMapping("/list")
    public List<UserEntity> findAllUser() {
        return userServiceBySofaHessian.findAll();
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userServiceBySofaHessian.deleteById(id);
        return "success";
    }

}
