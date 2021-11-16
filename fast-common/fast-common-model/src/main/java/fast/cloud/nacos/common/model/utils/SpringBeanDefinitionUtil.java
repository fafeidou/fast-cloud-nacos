package fast.cloud.nacos.common.model.utils;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class SpringBeanDefinitionUtil {

    private static BeanDefinitionRegistry beanRegistry;

    private static ConfigurableListableBeanFactory beanFactory;

    public static BeanDefinitionRegistry getBeanRegistry() {
        return beanRegistry;
    }

    public static void setBeanRegistry(BeanDefinitionRegistry beanRegistry) {
        SpringBeanDefinitionUtil.beanRegistry = beanRegistry;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        SpringBeanDefinitionUtil.beanFactory = beanFactory;
    }

}
