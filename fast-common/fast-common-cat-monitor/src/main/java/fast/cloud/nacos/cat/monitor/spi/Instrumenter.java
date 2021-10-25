package fast.cloud.nacos.cat.monitor.spi;

import net.bytebuddy.agent.builder.AgentBuilder;

public interface Instrumenter {
    AgentBuilder instrument(AgentBuilder agentBuilder);
}