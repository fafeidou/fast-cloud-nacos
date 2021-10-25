package fast.cloud.nacos.cat.monitor.enhance;


import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import fast.cloud.nacos.cat.monitor.annotation.Monitor;
import fast.cloud.nacos.cat.monitor.common.JsonUtils;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;

public class MonitorInstrumenter implements Instrumenter {

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(ElementMatchers.nameStartsWith("fast.cloud")).transform(new Transformer()).asTerminalTransformation();
    }

    private static class Transformer implements AgentBuilder.Transformer {

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {
            return builder.visit(Advice.to(Interceptor.class).on(isAnnotatedWith(ElementMatchers.named("fast.cloud.nacos.cat.monitor.annotation.Monitor"))));
        }
    }

    public static class Interceptor {

        @Advice.OnMethodEnter(inline = false)
        public static Transaction enter(@Advice.AllArguments Object[] args, @Advice.Origin Method method) {

            Monitor monitor = method.getAnnotation(Monitor.class);
            Transaction transaction = Cat.newTransaction(monitor.type(), monitor.name());
            if (monitor.isSaveParam()) {
                if (args != null) {
                    transaction.addData("params", JsonUtils.toJson(args));
                }
            }
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
                    transaction.setStatus(Message.SUCCESS);
                }
            } finally {
                transaction.complete();
            }
        }
    }
}