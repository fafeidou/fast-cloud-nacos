package fast.cloud.nacos.grpc.starter.client.controller;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceByProtoStuff;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/user")
public class V2UserController {

    private final UserServiceByProtoStuff userServiceByProtoStuff;

    @PostMapping("/add")
    public UserEntity insertUser(@RequestBody UserEntity userEntity){
        userServiceByProtoStuff.insert(userEntity);
        return userEntity;
    }

    @GetMapping("/list")
    public List<UserEntity> findAllUser(){
        return userServiceByProtoStuff.findAll();
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id){
        userServiceByProtoStuff.deleteById(id);
        return "success";
    }

}
