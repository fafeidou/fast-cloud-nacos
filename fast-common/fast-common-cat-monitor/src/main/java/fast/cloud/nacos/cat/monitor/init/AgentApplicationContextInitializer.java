package fast.cloud.nacos.cat.monitor.init;

import fast.cloud.nacos.cat.monitor.listener.AgentListener;
import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.instrument.Instrumentation;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author qinfuxiang
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AgentApplicationContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final AtomicBoolean AGENT_START = new AtomicBoolean(false);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (AGENT_START.compareAndSet(false, true)) {
            Instrumentation inst = ByteBuddyAgent.install();
            AgentBuilder agentBuilder = new AgentBuilder.Default()
                    .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION).with(new AgentListener());


            Instrumenter instrumenter;
            for (Iterator iterator = ServiceLoader.load(Instrumenter.class).iterator(); iterator.hasNext();
                 agentBuilder = instrumenter.instrument(agentBuilder)) {
                instrumenter = (Instrumenter) iterator.next();
            }

            agentBuilder.installOn(inst);
        }
    }
}