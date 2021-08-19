package fast.cloud.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationListenerImpl implements ApplicationListener<ApplicationEvent> {

    public ApplicationListenerImpl() {
        System.out.println("ApplicationListenerImpl#constructor");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("ApplicationListenerImpl#" + event.getClass().getSimpleName());
    }
}