package fast.cloud.nacos.prometheus.example.controller;

import fast.cloud.nacos.prometheus.example.aop.ApiCount;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinfuxiang
 * @since 2020/11/18 12:49
 */
@RestController
public class TestController {

    @ApiCount
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
