package fast.cloud.nacos.cat.monitor.enhance;

import fast.cloud.nacos.cat.monitor.bootstrap.ContextTrampoline;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public class ExecutorInstrumenter implements Instrumenter {

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(ElementMatchers.isSubTypeOf(Executor.class).and(ElementMatchers.not(ElementMatchers.isAbstract()))).transform(new Transformer());
    }

    public static class Interceptor {
        public Interceptor() {
        }

        @Advice.OnMethodEnter(
            inline = true
        )
        public static void enter(@Advice.Argument(value = 0,readOnly = false) Runnable runnable, @Advice.This Object obj, @Advice.Origin
            Method method) {
            ContextTrampoline.wrapInCurrentContext(runnable, obj, method);
        }
    }

    private static class Transformer implements AgentBuilder.Transformer {
        private Transformer() {
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            return builder.visit(Advice.to(Interceptor.class).on(ElementMatchers.named("execute").and(ElementMatchers.takesArguments(1).and(ElementMatchers.takesArgument(0, Runnable.class)))));
        }
    }
}

