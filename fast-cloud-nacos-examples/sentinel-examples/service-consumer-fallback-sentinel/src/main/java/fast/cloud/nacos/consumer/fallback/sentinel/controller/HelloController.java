package fast.cloud.nacos.consumer.fallback.sentinel.controller;

import fast.cloud.nacos.consumer.fallback.sentinel.feign.HelloRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class HelloController {
    @Autowired
    HelloRemote helloRemote;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/helloByFeign")
    public String helloByFeign() {
        return helloRemote.hello();
    }

    @GetMapping("/helloByRestTemplate")
    public String helloByRestTemplate() {
        return restTemplate.getForObject("http://service-provider-sentinel/hello", String.class);
    }
}
