package fast.cloud.nacos.grpc.starter.spring;

import com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor;
import fast.cloud.nacos.grpc.starter.annotation.GrpcReference;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor implements ApplicationContextAware {

    /**
     * The bean name of {@link ReferenceAnnotationBeanPostProcessor}
     */
    public static final String BEAN_NAME = "referenceAnnotationBeanPostProcessor";

    private static final int CACHE_SIZE = Integer.getInteger(BEAN_NAME + ".cache.size", 32);

    private final ConcurrentHashMap<String, InvocationHandler> localReferenceBeanInvocationHandlerCache =
            new ConcurrentHashMap<>(CACHE_SIZE);

    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public ReferenceAnnotationBeanPostProcessor() {
        super(GrpcReference.class);
    }


    /**
     * inject proxy objectAnnotationInjectedBeanPostProcessor into the SOAReference identification
     */
    @Override
    protected Object doGetInjectedBean(AnnotationAttributes annotationAttributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) {
        Object proxy = getBeanFactory().getBean(injectedType);
        return proxy;
    }


    /**
     * AnnotationAttributes hashCode as the inject bean key in cache
     */
    @Override
    protected String buildInjectedObjectCacheKey(AnnotationAttributes annotationAttributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) {
        Assert.isTrue(injectedType.isInterface(), "@GrpcReference can only be specified on an interface");
        Assert.isTrue(injectedType.getAnnotation(GrpcService.class) != null, "@GrpcReference can only be specified on an interface what has @GrpcService Annotation");
        return injectedType.getName() + "@GrpcReference:" + annotationAttributes.hashCode();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
