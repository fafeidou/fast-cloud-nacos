package fast.cloud.nacos.cat.monitor.listener;


import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentListener implements AgentBuilder.Listener {
    private static final Logger logger = LoggerFactory.getLogger(AgentListener.class);

    public AgentListener() {
    }

    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
    }

    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
        logger.debug("On Transformation class {},classloader {}", typeDescription.getName(), classLoader);
    }

    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
    }

    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        logger.error("Enhance class " + typeName + " error,classloader " + classLoader, throwable);
    }

    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        logger.debug("On Complete class {},classloader {}", typeName, classLoader);
    }
}