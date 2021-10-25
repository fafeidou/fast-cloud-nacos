package fast.cloud.nacos.cat.monitor.bootstrap;

import fast.cloud.nacos.cat.monitor.spi.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;

public final class ContextTrampolineInitializer implements Instrumenter {
    public ContextTrampolineInitializer() {
    }

    @Override
    public AgentBuilder instrument(AgentBuilder agentBuilder) {
        ContextTrampoline.setContextStrategy(new ContextStrategyImpl());
        return agentBuilder;
    }
}
