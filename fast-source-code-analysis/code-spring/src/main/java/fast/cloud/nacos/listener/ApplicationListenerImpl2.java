package fast.cloud.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class ApplicationListenerImpl2 implements ApplicationListener<ApplicationEvent> {

    public ApplicationListenerImpl2() {
        System.out.println("ApplicationListenerImpl#constructor");
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("ApplicationListenerImpl2#" + event.getClass().getSimpleName());
    }
}