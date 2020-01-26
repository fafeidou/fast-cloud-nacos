package fast.cloud.nacos.tenant.event;

import fast.cloud.nacos.tenant.entity.DemoEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DemoEventHandler {
    @EventListener(classes = DemoEvent.class)
    public void handDemandPublishedEvent(DemoEvent demoEvent) {
        DemoEntity demoEntity = (DemoEntity) demoEvent.getSource();
        System.out.println(demoEntity);

    }
}
