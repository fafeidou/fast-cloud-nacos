package fast.boot.autoconfigure.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author qinfuxiang
 */
public class HelloWorldBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(HelloWorldConfiguration.class);
//        registry.registerBeanDefinition(HelloWorldConfiguration.class.getName(), bdb.getBeanDefinition());
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bdb.getBeanDefinition(), HelloWorldConfiguration.class.getName(),
                null);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
