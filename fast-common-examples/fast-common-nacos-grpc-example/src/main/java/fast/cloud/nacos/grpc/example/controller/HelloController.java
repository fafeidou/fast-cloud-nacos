package fast.cloud.nacos.grpc.example.controller;

import fast.cloud.nacos.grpc.example.service.client.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/{name}")
    public String sayHello(@PathVariable String name) {
        return helloService.hello(name);
    }
}
