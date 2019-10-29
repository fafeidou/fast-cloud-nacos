package fast.cloud.nacos.cloud.examples.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dubbo-spring-cloud-provider")
public interface UserRemote {
    @GetMapping("/test")
    String hello(@RequestParam("name") String name);
}
