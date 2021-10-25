package fast.cloud.nacos.cat.monitor.bootstrap;

import java.lang.reflect.Method;

public class ContextTrampoline {
    private static ContextStrategy contextStrategy;

    private ContextTrampoline() {
    }

    public static void setContextStrategy(ContextStrategy contextStrategy) {
        ContextTrampoline.contextStrategy = contextStrategy;
    }

    public static Runnable wrapInCurrentContext(Runnable runnable, Object obj, Method originMethod) {
        return contextStrategy.wrapInCurrentContext(runnable, obj, originMethod);
    }
}