package fast.cloud.nacos.fastcommongrpcexample.controller;

import fast.cloud.nacos.fastcommongrpcexample.service.IHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @Autowired
    private IHelloService helloService;

    @GetMapping("/{name}")
    public String sayHello(@PathVariable String name) {
        return helloService.sayHello(name);
    }
}
