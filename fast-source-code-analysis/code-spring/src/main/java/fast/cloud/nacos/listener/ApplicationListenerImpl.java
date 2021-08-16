package fast.cloud.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationListenerImpl implements ApplicationListener<ApplicationEvent> {

    public ApplicationListenerImpl() {
        log.info("ApplicationListenerImpl#constructor");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("ApplicationListenerImpl#" + event.getClass().getSimpleName());
    }
}