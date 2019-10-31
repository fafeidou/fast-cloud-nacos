package fast.cloud.nacos.sentinel.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider-sentinel")
public interface HelloRemote {
    @GetMapping("/hello")
    String hello();
}
