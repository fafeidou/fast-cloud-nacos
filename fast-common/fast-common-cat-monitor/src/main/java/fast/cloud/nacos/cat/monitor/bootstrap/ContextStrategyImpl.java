package fast.cloud.nacos.cat.monitor.bootstrap;


import com.dianping.cat.Cat;
import fast.cloud.nacos.cat.monitor.context.ContextManager;
import fast.cloud.nacos.cat.monitor.context.RuntimeContextSnapshot;

import java.lang.reflect.Method;

public class ContextStrategyImpl implements ContextStrategy {

    public static ThreadLocal<Cat.Context> contextThreadLocal = new ThreadLocal<>();

    @Override
    public Runnable wrapInCurrentContext(Runnable runnable, Object obj, Method originMethod) {
        if (Cat.isEnabled() && Cat.isInitialized() && !ignoreThread(obj, runnable)) {
            Cat.Context context = null;
            Cat.Context parentContext = contextThreadLocal.get();
            RuntimeContextSnapshot snapshot = ContextManager.getRuntimeContext().capture();
            if (parentContext != null) {
                context = parentContext;
            } else {
                context = new RunnableWrapper.RunnableContext();
            }
            Cat.logRemoteCallClient(context,Cat.getManager().getDomain()+"-!");
            return new RunnableWrapper(
                context,
                snapshot,
                runnable,
                obj,
                originMethod);
        }
        return runnable;
    }

    private boolean ignoreThread(Object obj, Runnable runnable) {
        if (obj == null) {
            return false;
        }
        if (runnable == null) {
            return true;
        }
        String name = obj.getClass().getName();
        String runnableName = runnable.getClass().getName();
        if (name.startsWith("org.apache.tomcat.util")
            || runnableName.startsWith("com.xxl.job.core.server.EmbedServer$EmbedHttpServerHandler")
            || runnableName.startsWith("org.apache.curator.framework.listen.ListenerContainer")
            || runnableName.startsWith("com.mongodb.internal.connection")
            || runnableName.startsWith("sun.")
            || runnableName.startsWith("org.apache.skywalking")
            || runnableName.startsWith("com.zaxxer.hikari.pool.HikariPool")
            || runnableName.startsWith("com.mysql.jdbc.ConnectionImpl")
            || runnableName.startsWith("okhttp3.ConnectionPool")
            || runnableName.startsWith("io.netty.util.concurrent")
            || runnableName.startsWith("com.rabbitmq.client.impl.ConsumerWorkService")
            || runnableName.startsWith("org.redisson.client.handler.CommandPubSubDecoder")
            || runnableName.startsWith("org.apache.phoenix.job.JobManager")
            || runnableName.startsWith("org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer")){
            return true;
        }

        return false;
    }
}