package fast.cloud.nacos.grpc.starter.client.controller;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceByFastJSON;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v3/user")
public class V3UserController {

    private final UserServiceByFastJSON userServiceByFastJSON;

    @PostMapping("/add")
    public UserEntity insertUser(@RequestBody UserEntity userEntity){
//        userServiceByFastJSON.insert(JSONObject.toJSONString(userEntity));
        return userEntity;
    }

    @GetMapping("/list")
    public List<UserEntity> findAllUser(){
        return userServiceByFastJSON.findAll();
    }

    @PostMapping("/remove")
    public String removeUser(@RequestParam("id") Long id){
        userServiceByFastJSON.deleteById(String.valueOf(id));
        return "success";
    }

}
