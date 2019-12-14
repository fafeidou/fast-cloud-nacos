package fast.cloud.nacos.securityprovider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("security")
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "hello world oauth2";
    }
}
