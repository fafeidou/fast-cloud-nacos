package fast.cloud.nacos.grpc.starter.spring;

import com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor;
import fast.cloud.nacos.grpc.starter.annotation.GrpcService;
import fast.cloud.nacos.grpc.starter.annotation.GrpcReference;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;

public class ReferenceAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

    /**
     * The bean name of {@link ReferenceAnnotationBeanPostProcessor}
     */
    public static final String BEAN_NAME = "referenceAnnotationBeanPostProcessor";

    @SuppressWarnings("unchecked")
    public ReferenceAnnotationBeanPostProcessor() {
        super(GrpcReference.class);
    }


    /**
     * inject proxy object into the SOAReference identification
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
        return injectedType.getName() + "@Reference:" + annotationAttributes.hashCode();
    }

}
