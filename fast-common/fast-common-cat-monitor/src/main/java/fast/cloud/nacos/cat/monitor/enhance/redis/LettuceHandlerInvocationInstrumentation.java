package fast.cloud.nacos.cat.monitor.enhance.redis;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static net.bytebuddy.matcher.ElementMatchers.named;

@SuppressWarnings("unchecked")
public class LettuceHandlerInvocationInstrumentation implements Instrumenter {

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(
            named("io.lettuce.core.cluster.ClusterFutureSyncInvocationHandler")
                .or(named("io.lettuce.core.FutureSyncInvocationHandler"))).transform(new Transformer());
    }

    private static class Transformer implements AgentBuilder.Transformer {
        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {
            return builder.visit(Advice.to(Interceptor.class).on(
                named("handleInvocation")

            ));
        }
    }

    public static class Interceptor {
        @Advice.OnMethodEnter(inline = false)
        public static Transaction enter(@Advice.Origin Method method,
                                        @Advice.This Object obj,
                                        @Advice.AllArguments Object[] allArguments) throws Exception {
            Method targetMethod = (Method) allArguments[1];
            Object[] args = (Object[]) allArguments[2];
            String operationName = "Lettuce/Sync/";
            StringBuilder argsStr = new StringBuilder();
            Transaction transaction = null;
            try {
                transaction = Cat.newTransaction("Redis", operationName + targetMethod.getName().toUpperCase());
                for (Object arg : args) {
                    if (arg instanceof byte[]) {
                        String argStr = new String((byte[]) arg, Charset.defaultCharset());
                        argsStr.append(argStr);
                    }
                }
            } catch (Exception e) {
                Cat.logError(e);
            }
            transaction.addData("args", argsStr);
            return transaction;
        }


        @Advice.OnMethodExit(onThrowable = Throwable.class, inline = false)
        public static void exit(@Advice.Enter Transaction transaction,
                                @Advice.Origin Method method,
                                @Advice.Thrown Throwable throwable) {
            try {
                if (throwable != null) {
                    Cat.logError(throwable);
                    transaction.setStatus(throwable);
                } else {
                    transaction.setStatus(Transaction.SUCCESS);
                }
            } finally {
                transaction.complete();
            }
        }

    }
}
