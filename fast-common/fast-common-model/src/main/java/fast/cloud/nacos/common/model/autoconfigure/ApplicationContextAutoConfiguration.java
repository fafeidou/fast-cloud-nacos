package fast.cloud.nacos.common.model.autoconfigure;

import fast.cloud.nacos.common.model.utils.ApplicationContextUtil;
import fast.cloud.nacos.common.model.utils.EnvironmentUtil;
import fast.cloud.nacos.common.model.utils.SpringBeanDefinitionUtil;
import fast.cloud.nacos.common.model.utils.StringValueResolverUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringValueResolver;

@Configuration
public class ApplicationContextAutoConfiguration
        implements ApplicationContextAware, EnvironmentPostProcessor, BeanDefinitionRegistryPostProcessor, EmbeddedValueResolverAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        EnvironmentUtil.setEnvironment(environment);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringBeanDefinitionUtil.setBeanFactory(beanFactory);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        SpringBeanDefinitionUtil.setBeanRegistry(registry);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        StringValueResolverUtils.setResolver(resolver);
    }

}
