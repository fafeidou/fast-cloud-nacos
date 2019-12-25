package fast.cloud.nacos.securityconsumer.controller;

import fast.cloud.nacos.securityapi.ServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class HelloController {

    @Autowired
    private ServiceHi serviceHi;

    @GetMapping("hello")
    public String hello() {
        return serviceHi.hello();
    }

    @GetMapping("hello2")
    public String hello2() {
        return serviceHi.hello2();
    }
}
