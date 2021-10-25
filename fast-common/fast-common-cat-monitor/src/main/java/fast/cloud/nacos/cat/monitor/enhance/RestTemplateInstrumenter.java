package fast.cloud.nacos.cat.monitor.enhance;

import fast.cloud.nacos.cat.monitor.prometheus.RestTemplateMonitor;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * @author qinfuxiang
 */
public class RestTemplateInstrumenter implements Instrumenter {
    public static final String HTTP_REST_TEMPLATE_CLASS_NAME = "org.springframework.web.client.RestTemplate";
    public static final String HTTP_REQUEST_CLASS_NAME = "org.springframework.http.client.AbstractClientHttpRequest";

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(ElementMatchers.named("org.springframework.http.client.AbstractClientHttpRequest")).transform(new RestTemplateInstrumenter.Transformer());
    }

    public static class Interceptor {

        @Advice.OnMethodEnter(inline = false)
        public static void enter() {
            RestTemplateMonitor.start();
        }

        @Advice.OnMethodExit(
                onThrowable = Throwable.class,inline = false
        )
        public static void exit(@This Object ths, @Advice.Return Object result, @Advice.Thrown Throwable t) {
            RestTemplateMonitor.metric(ths, result, t);
        }
    }

    private static class Transformer implements AgentBuilder.Transformer {
        private Transformer() {
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            //TODO 去掉ElementMatchers.takesArguments(0) 解析参数异常
            return builder.visit(Advice.to(ExecutorInstrumenter.Interceptor.class).on(ElementMatchers.named("execute").and(ElementMatchers.takesArguments(0))));
        }
    }
}
