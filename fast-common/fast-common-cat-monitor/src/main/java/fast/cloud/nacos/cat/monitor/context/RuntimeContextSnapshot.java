package fast.cloud.nacos.cat.monitor.context;

import java.util.Iterator;
import java.util.Map;

public class RuntimeContextSnapshot {
    private final Map<Object, Object> map;

    public RuntimeContextSnapshot(Map<Object, Object> map) {
        this.map = map;
    }

    public Iterator<Map.Entry<Object, Object>> iterator() {
        return map.entrySet().iterator();
    }
}