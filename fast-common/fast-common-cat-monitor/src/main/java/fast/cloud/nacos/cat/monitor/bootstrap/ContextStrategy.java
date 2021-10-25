package fast.cloud.nacos.cat.monitor.bootstrap;

import java.lang.reflect.Method;

public interface ContextStrategy {
    Runnable wrapInCurrentContext(Runnable runnable, Object obj, Method method);
}
