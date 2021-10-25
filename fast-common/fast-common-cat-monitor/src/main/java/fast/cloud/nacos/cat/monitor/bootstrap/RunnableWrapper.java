package fast.cloud.nacos.cat.monitor.bootstrap;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.context.ContextManager;
import fast.cloud.nacos.cat.monitor.context.RuntimeContextSnapshot;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RunnableWrapper implements Runnable {

    private static final String DEFAULT_ASYNC_TYPE = "async";

    private Runnable runnable;

    private RuntimeContextSnapshot contextSnapshot;

    private Cat.Context context;

    private Object obj;


    private Method originMethod;

    public RunnableWrapper(Cat.Context context, RuntimeContextSnapshot snapshot, Runnable runnable,
                           Object obj,
                           Method originMethod) {
        this.context = context;
        this.runnable = runnable;
        this.obj = obj;
        this.originMethod = originMethod;
        this.contextSnapshot = snapshot;
    }

    @Override
    public void run() {
        Transaction t = null;
        try {
            t = Cat.newTransaction(DEFAULT_ASYNC_TYPE, runnable.getClass().getName());
            Cat.logRemoteCallServer(context);
            ContextManager.getRuntimeContext().accept(contextSnapshot);
            t.addData("obj", obj.getClass());
            ContextStrategyImpl.contextThreadLocal.set(context);
            MDC.put("traceId", context.getProperty(Cat.Context.ROOT));
            runnable.run();
        } catch (Throwable e) {
            Cat.logError(e);
            throw e;
        } finally {
            if (t != null) {
                t.setStatus(Transaction.SUCCESS);
                t.complete();
                ContextStrategyImpl.contextThreadLocal.remove();
                MDC.remove("traceId");
                ContextManager.getRuntimeContext().clear();
            }
        }

    }


    public static class RunnableContext implements Cat.Context {

        private Map<String, String> properties;

        @Override
        public void addProperty(String key, String value) {
            if (properties == null) {
                properties = new HashMap<>();
            }
            properties.put(key, value);
        }

        @Override
        public String getProperty(String key) {
            if (properties == null) {
                return null;
            }
            return properties.get(key);
        }
    }
}