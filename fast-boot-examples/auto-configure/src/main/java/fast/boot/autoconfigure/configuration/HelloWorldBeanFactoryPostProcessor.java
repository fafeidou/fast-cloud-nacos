package fast.boot.autoconfigure.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * @author qinfuxiang
 */
public class HelloWorldBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(HelloWorldConfiguration.class);
        beanFactory.registerSingleton(HelloWorldConfiguration.class.getName(), bdb.getBeanDefinition());
    }
}
