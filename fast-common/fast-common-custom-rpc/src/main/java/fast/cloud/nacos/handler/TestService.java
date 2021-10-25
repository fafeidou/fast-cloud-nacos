package fast.cloud.nacos.handler;

import fast.cloud.nacos.annoation.CustomServiceAutoImpl;
import org.springframework.stereotype.Service;

@CustomServiceAutoImpl(additionalPaths = "demo/request")
@Service
public class TestService {
    public String test() {
        return "hello";
    }
}
