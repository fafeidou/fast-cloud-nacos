package fast.cloud.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class ApplicationListenerImpl2 implements ApplicationListener<ApplicationEvent> {

    public ApplicationListenerImpl2() {
        log.info("ApplicationListenerImpl#constructor");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("ApplicationListenerImpl2#" + event.getClass().getSimpleName());
    }
}