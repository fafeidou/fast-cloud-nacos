package fast.cloud.nacos.handler;

import fast.cloud.nacos.annoation.CustomServiceAutoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.*;

@Slf4j
public class AutoDemoServiceImplExporter implements BeanFactoryPostProcessor {
    private static final String PATH_PREFIX = "/";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        //查找@CustomServiceAutoImpl注解
        Map<String, String> serviceBeanDefinitions = findServiceBeanDefinitions(defaultListableBeanFactory);
        //根据注解注入到Spring ioc容器中
        for (Map.Entry<String, String> entry : serviceBeanDefinitions.entrySet()) {
            registerServiceProxy(defaultListableBeanFactory, makeUrlPath(entry.getKey()), entry.getValue());
        }
    }

    private String makeUrlPath(String servicePath) {
        if (null == servicePath || 0 == servicePath.length()) {
            throw new IllegalArgumentException("the service path must be provided");
        }

        if ('/' == servicePath.charAt(0)) {
            return servicePath;
        }

        return PATH_PREFIX.concat(servicePath);
    }

    private void registerServiceProxy(DefaultListableBeanFactory defaultListableBeanFactory, String servicePath, String serviceBeanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DemoServiceExporter.class).addPropertyReference("service", serviceBeanName);
        defaultListableBeanFactory.registerBeanDefinition(servicePath, builder.getBeanDefinition());
    }

    private BeanDefinition findBeanDefinition(ConfigurableListableBeanFactory beanFactory, String serviceBeanName) {
        if (beanFactory.containsLocalBean(serviceBeanName)) {
            return beanFactory.getBeanDefinition(serviceBeanName);
        }
        BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
        if (parentBeanFactory != null && ConfigurableListableBeanFactory.class.isInstance(parentBeanFactory)) {
            return findBeanDefinition((ConfigurableListableBeanFactory) parentBeanFactory, serviceBeanName);
        }
        throw new NoSuchBeanDefinitionException(serviceBeanName);
    }

    private static Map<String, String> findServiceBeanDefinitions(ConfigurableListableBeanFactory beanFactory) {
        final Map<String, String> serviceBeanNames = new HashMap<>();

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            CustomServiceAutoImpl autoJsonRpcServiceImplAnnotation = beanFactory.findAnnotationOnBean(beanName, CustomServiceAutoImpl.class);
            if (null != autoJsonRpcServiceImplAnnotation) {
                List<String> paths = new ArrayList<>();
                Collections.addAll(paths, autoJsonRpcServiceImplAnnotation.additionalPaths());

                for (String path : paths) {
                    if (isNotDuplicateService(serviceBeanNames, beanName, path)) {
                        serviceBeanNames.put(path, beanName);
                    }
                }
            }
        }
        //递归遍历查看父factory里面是包含
        collectFromParentBeans(beanFactory, serviceBeanNames);
        return serviceBeanNames;
    }

    private static void collectFromParentBeans(ConfigurableListableBeanFactory beanFactory, Map<String, String> serviceBeanNames) {
        BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
        if (parentBeanFactory != null && ConfigurableListableBeanFactory.class.isInstance(parentBeanFactory)) {
            for (Map.Entry<String, String> entry : findServiceBeanDefinitions((ConfigurableListableBeanFactory) parentBeanFactory).entrySet()) {
                if (isNotDuplicateService(serviceBeanNames, entry.getKey(), entry.getValue())) {
                    serviceBeanNames.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private static boolean isNotDuplicateService(Map<String, String> serviceBeanNames, String beanName, String pathValue) {
        if (serviceBeanNames.containsKey(pathValue)) {
            String otherBeanName = serviceBeanNames.get(pathValue);
            log.debug("Duplicate Custom-RPC path specification: found {} on both [{}] and [{}].", pathValue, beanName, otherBeanName);
            return false;
        }
        return true;
    }
}
