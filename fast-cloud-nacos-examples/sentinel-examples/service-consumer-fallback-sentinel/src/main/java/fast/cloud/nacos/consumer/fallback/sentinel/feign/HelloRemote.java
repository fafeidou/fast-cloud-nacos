package fast.cloud.nacos.consumer.fallback.sentinel.feign;

import fast.cloud.nacos.consumer.fallback.sentinel.fallback.HelloRemoteFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "service-provider-sentinel", fallback = HelloRemoteFallBack.class)
public interface HelloRemote {
    @GetMapping("/hello")
    String hello();
}
