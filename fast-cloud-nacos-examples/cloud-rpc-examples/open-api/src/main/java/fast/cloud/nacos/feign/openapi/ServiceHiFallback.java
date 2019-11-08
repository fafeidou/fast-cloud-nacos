package fast.cloud.nacos.feign.openapi;

import org.springframework.stereotype.Component;

@Component
public class ServiceHiFallback implements ServiceHi{
    @Override
    public String sayHiFromClientOne(String name) {
        return "this is service hi fallback";
    }
}
