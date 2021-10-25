package fast.cloud.nacos.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.support.RemoteExporter;

public abstract class AbstractDemoServiceExporter extends RemoteExporter implements InitializingBean, ApplicationContextAware {

    private ObjectMapper objectMapper;
    private CustomRpcServer customRpcServer;
    private ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (objectMapper == null && applicationContext != null && applicationContext.containsBean("objectMapper")) {
            objectMapper = (ObjectMapper) applicationContext.getBean("objectMapper");
        }
        if (objectMapper == null && applicationContext != null) {
            try {
                objectMapper = BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, ObjectMapper.class);
            } catch (Exception e) {
                logger.debug(e);
            }
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        // Create the server. The 'handler' parameter here is either a proxy or the real instance depending on
        // the presence or absence of the interface. This is because it is not possible to create a proxy unless
        // an interface is specified.

        customRpcServer = new CustomRpcServer(objectMapper,
                null == getServiceInterface() ? getService() : getProxyForService(),
                getServiceInterface());
        exportService();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CustomRpcServer getJsonRpcServer() {
        return customRpcServer;
    }

    public void setJsonRpcServer(CustomRpcServer customRpcServer) {
        this.customRpcServer = customRpcServer;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    void exportService()
            throws Exception {
        // no-op
    }
}
