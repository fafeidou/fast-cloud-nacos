package fast.cloud.nacos.tenant;

import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.event.DemoEvent;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FastCommonTenantExampleApplicationTests {

    @Test
    public void testEvent() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("name");
        demoEntity.setId(11l);
        DemoEvent demoEvent = new DemoEvent(demoEntity);
        demoEvent.publish();
    }

}
