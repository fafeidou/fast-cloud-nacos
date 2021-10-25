package fast.cloud.nacos.cat.monitor.enhance;

import fast.cloud.nacos.cat.monitor.prometheus.HttpClientMonitor;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author qinfuxiang
 */
public class HttpClientInstrumenter implements Instrumenter {
    public static final String HTTP_REST_TEMPLATE_CLASS_NAME = "org.springframework.web.client.RestTemplate";
    public static final String HTTP_REQUEST_CLASS_NAME = "org.springframework.http.client.AbstractClientHttpRequest";

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        return agentBuilder.type(ElementMatchers.hasSuperType(named("feign.httpclient.ApacheHttpClient")).and(ElementMatchers.not(ElementMatchers.isAbstract()))).transform(new HttpClientInstrumenter.Transformer());
    }

    public static class Interceptor {

        @Advice.OnMethodEnter(
                inline = false
        )
        public static void enter(@Advice.This Object obj, @Advice.Origin Method method) {
            System.out.println("feign.Client enter");
            HttpClientMonitor.start();
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class, inline = false)
        public static void exit(@This Object ths, @Advice.Return Object result, @Advice.Thrown Throwable t) {
            System.out.println("feign.Client exit");
            HttpClientMonitor.metric(ths, result, t);
        }
    }

    private static class Transformer implements AgentBuilder.Transformer {
        private Transformer() {
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            return builder.visit(Advice.to(HttpClientInstrumenter.Interceptor.class).on(ElementMatchers.named("execute")));
        }
    }
}
