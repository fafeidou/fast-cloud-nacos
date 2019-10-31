package fast.cloud.nacos.consumer.fallback.sentinel.fallback;

import fast.cloud.nacos.consumer.fallback.sentinel.feign.HelloRemote;
import org.springframework.stereotype.Component;

@Component
public class HelloRemoteFallBack implements HelloRemote {
    @Override
    public String hello() {
        return "Feign FallBack Msg";
    }
}
