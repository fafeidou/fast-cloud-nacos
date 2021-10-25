package fast.cloud.nacos.cat.monitor.context;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeContext {
    private final ThreadLocal<RuntimeContext> contextThreadLocal;
    private Map<Object, Object> context = new ConcurrentHashMap<>(0);

    public RuntimeContext(ThreadLocal<RuntimeContext> contextThreadLocal) {
        this.contextThreadLocal = contextThreadLocal;
    }

    public void put(Object key, Object value) {
        context.put(key, value);
    }

    public Object get(Object key) {
        return context.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        return (T) context.get(key);
    }

    public void remove(Object key) {
        context.remove(key);
        if (context.isEmpty()) {
            contextThreadLocal.remove();
        }
    }

    public Set<Object> keys(){
        return context.keySet();
    }

    public void clear() {
        this.context.clear();
        contextThreadLocal.remove();
    }

    public RuntimeContextSnapshot capture() {
        Map<Object, Object> runtimeContextMap = new HashMap<>();
        for (Object key : this.context.keySet()) {
            Object value = this.get(key);
            if (value != null) {
                runtimeContextMap.put(key, value);
            }
        }
        return new RuntimeContextSnapshot(runtimeContextMap);
    }

    public void accept(RuntimeContextSnapshot snapshot) {
        Iterator<Map.Entry<Object, Object>> iterator = snapshot.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> runtimeContextItem = iterator.next();
            ContextManager.getRuntimeContext().put(runtimeContextItem.getKey(), runtimeContextItem.getValue());
        }
    }
}
