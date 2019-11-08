package fast.cloud.nacos.feign.openapi;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceHiFactory implements FallbackFactory<ServiceHi> {
    @Override
    public ServiceHi create(Throwable cause) {
        return name -> "service hi fallback";
    }
}
