package fast.cloud.nacos.provider.controller;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceByProtoStuff;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v2/user")
public class V2UserController {

    @Resource
    private UserServiceByProtoStuff userServiceByProtoStuff;

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
