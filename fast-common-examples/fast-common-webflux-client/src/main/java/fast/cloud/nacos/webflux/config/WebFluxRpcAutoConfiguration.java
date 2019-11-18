package fast.cloud.nacos.webflux.config;

import fast.cloud.nacos.webflux.ApiServer;
import fast.cloud.nacos.webflux.annotation.ApiServerScan;
import fast.cloud.nacos.webflux.proxys.JDKProxyCreator;
import fast.cloud.nacos.webflux.utils.ClassNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Configuration
public class WebFluxRpcAutoConfiguration {

    /**
     * 手动扫描 @GrpcService 注解的接口，生成动态代理类，注入到 Spring 容器
     */
    public static class ExternalApiServerScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            ClassPathBeanDefinitionScanner scanner = new ClassPathApiServerScanner(registry);
            scanner.setResourceLoader(this.resourceLoader);
            scanner.addIncludeFilter(new AnnotationTypeFilter(ApiServer.class));
            Set<BeanDefinition> beanDefinitions = scanPackages(importingClassMetadata, scanner);
            ProxyUtil.registerBeans(beanFactory, beanDefinitions);
        }

        /**
         * 包扫描
         */
        private Set<BeanDefinition> scanPackages(AnnotationMetadata importingClassMetadata, ClassPathBeanDefinitionScanner scanner) {
            List<String> packages = new ArrayList<>();
            Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ApiServerScan.class.getCanonicalName());
            if (annotationAttributes != null) {
                String[] basePackages = (String[]) annotationAttributes.get("packages");
                if (basePackages.length > 0) {
                    packages.addAll(Arrays.asList(basePackages));
                }
            }
            Set<BeanDefinition> beanDefinitions = new HashSet<>();
            if (CollectionUtils.isEmpty(packages)) {
                return beanDefinitions;
            }
            packages.forEach(pack -> beanDefinitions.addAll(scanner.findCandidateComponents(pack)));
            return beanDefinitions;
        }

    }

    protected static class ClassPathApiServerScanner extends ClassPathBeanDefinitionScanner {

        ClassPathApiServerScanner(BeanDefinitionRegistry registry) {
            super(registry, false);
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        }

    }

    protected static class ProxyUtil {
        static void registerBeans(BeanFactory beanFactory, Set<BeanDefinition> beanDefinitions) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                if (StringUtils.isEmpty(className)) {
                    continue;
                }
                try {
                    // 创建代理类
                    Class<?> target = Class.forName(className);
                    Object invoker = new Object();
                    JDKProxyCreator jdkProxyCreator = new JDKProxyCreator();
                    Object proxy = jdkProxyCreator.createProxy(target);
//                    InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker);
//                    Object proxy = Proxy.newProxyInstance(GrpcService.class.getClassLoader(), new Class[]{target}, invocationHandler);

                    // 注册到 Spring 容器
                    String beanName = ClassNameUtils.beanName(className);
                    ((DefaultListableBeanFactory) beanFactory).registerSingleton(beanName, proxy);
                } catch (ClassNotFoundException e) {
                    log.warn("class not found : " + className);
                }
            }
        }
    }

}
