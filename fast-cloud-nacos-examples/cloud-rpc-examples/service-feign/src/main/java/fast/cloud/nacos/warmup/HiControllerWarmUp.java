package fast.cloud.nacos.warmup;

import fast.cloud.nacos.feign.openapi.ServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HiControllerWarmUp extends AbstractWarmUpComponent{
    @Autowired
    private ServiceHi serviceHi;

    @Override
    public void warmUp() throws Exception {
        doWarmUp("ServiceHi", "hello",
                () ->serviceHi.sayHiFromClientOne("bbb"),3);
    }
}
