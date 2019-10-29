package fast.cloud.nacos.cloud.examples.controller;

import fast.cloud.nacos.cloud.examples.remote.UserRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {
    @Autowired
    private UserRemote userRemote;

    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/test/feign")
    public String hello(String name) {
        return userRemote.hello(name);
    }

    @GetMapping("/test/restTemplate")
    public String restTemplate(String name) {
        return restTemplate.getForObject("http://dubbo-spring-cloud-provider/test?name="+name, String.class);
    }
}
