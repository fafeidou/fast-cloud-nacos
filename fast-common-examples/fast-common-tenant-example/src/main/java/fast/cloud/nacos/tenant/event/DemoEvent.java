package fast.cloud.nacos.tenant.event;

import fast.cloud.nacos.common.model.beans.event.BaseEvent;
import fast.cloud.nacos.tenant.entity.DemoEntity;

public class DemoEvent extends BaseEvent<DemoEntity> {

    public DemoEvent(DemoEntity source) {
        super(source);
    }
}
