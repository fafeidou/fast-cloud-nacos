package fast.cloud.nacos.controller;

import fast.cloud.nacos.feign.openapi.ServiceHi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author Batman.qin
 * @create 2018-11-23 19:37
 */
@RestController
@Slf4j
public class HiController {

    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    private ServiceHi serviceHi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping(value = "/hi")
    public String sayHi(@RequestParam String name) {
        log.info("服务名称{}","service-feign");
        return serviceHi.sayHiFromClientOne(name);
    }

    @GetMapping(value = "/hello")
    public String hello(@RequestParam String name) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String hello = serviceHi.hello(name);
        stopWatch.stop();
        System.out.println("stopWatch:"+ stopWatch.getTotalTimeMillis());
        return hello;
    }

    @GetMapping(value = "/hello2")
    public String hello2(@RequestParam String name) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("service-hi");
        URI uri = serviceInstance.getUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://service-hi/hello?name=" + name, String.class);
        return forEntity.getBody();
    }
}
