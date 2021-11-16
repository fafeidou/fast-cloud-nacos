package fast.cloud.nacos.common.kafka.config;

import com.google.common.collect.Maps;
import fast.cloud.nacos.common.kafka.producer.KafkaMessageProducer;
import fast.cloud.nacos.common.kafka.producer.KafkaTransactionMessageProducer;
import fast.cloud.nacos.common.model.utils.EnvironmentUtil;
import fast.cloud.nacos.common.model.utils.PropertiesUtil;
import fast.cloud.nacos.common.model.utils.SpringBeanDefinitionUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CustomKafkaMultipleProducerAutoConfiguration implements BeanFactoryPostProcessor, EnvironmentPostProcessor, BeanDefinitionRegistryPostProcessor {

    private String KEY_SOURCES = "spring.kafka.multiple.producer.sources";

    private String KEY_MULTIPLE = "spring.kafka.multiple.producer";

    private ConfigurableEnvironment environment;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        EnvironmentUtil.setEnvironment(environment);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringBeanDefinitionUtil.setBeanFactory(beanFactory);
        this.environment = EnvironmentUtil.getEnvironment();
        // 配置消息生产者
        configurationProducer((DefaultListableBeanFactory) beanFactory);
    }

    public void configurationProducer(DefaultListableBeanFactory beanFactory) {
        // 获取生产者配置 多个以，分割
        String sources = environment.getProperty(KEY_SOURCES);
        if (StringUtils.isEmpty(sources)) {
            return;
        }
        Arrays.asList(sources.trim().split(",")).forEach(producerName -> {
            Map<String, Object> producerConfigs = loadProducerConfigs(producerName);
            // 注册kafkaProducer
            this.registerMessageProducer(producerConfigs, producerName, beanFactory);
            // 注册kafkaProducerTransaction
            this.registerTransactionMessageProducer(producerConfigs, producerName, beanFactory);
            //注册KafkaTemplate
            SpringBeanDefinitionUtil.getBeanFactory().registerSingleton(producerName + "KafkaTemplate", kafkaTemplate(producerConfigs));
        });
    }

    private void registerMessageProducer(Map<String, Object> producerConfigs,
                                         String producerName, DefaultListableBeanFactory beanFactory) {
        MutablePropertyValues propertyValues = this.getPropertyValues(producerConfigs);
        // 注册kafkaProducer
        this.registerBean(beanFactory, producerName, KafkaMessageProducer.class, propertyValues);
    }

    private void registerTransactionMessageProducer(Map<String, Object> producerConfigs,
                                                    String producerName, DefaultListableBeanFactory beanFactory) {
        producerConfigs.computeIfPresent(ProducerConfig.RETRIES_CONFIG, (k, v) -> {
            if (v.toString().equals(BigDecimal.ZERO.toString())) {
                return null;
            }
            return v;
        });
        MutablePropertyValues propertyValues = this.getPropertyValues(producerConfigs);
        // 注册事务kafkaProducer
        this.registerBean(beanFactory, producerName + "Transaction", KafkaTransactionMessageProducer.class, propertyValues);
    }

    private KafkaTemplate<String, Object> kafkaTemplate(Map<String, Object> props) {
        return new KafkaTemplate<>(producerFactory(props));
    }

    private ProducerFactory<String, Object> producerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(producerConfig(props));
    }

    private Map<String, Object> producerConfig(Map<String, Object> props) {
        if (CollectionUtils.isEmpty(props)) {
            return null;
        }

        Set<String> set = Maps.newHashMap(props).keySet();
        for (String key : set) {
            if (key.indexOf("-") > -1) {
                Object value = props.get(key);
                if (null != value) {
                    String kkey = key.replaceAll("-", ".");
                    props.put(kkey, value);
                }
                props.remove(key);
            }
        }
        //TODO 发送者拦截器，可以做灰度消息
//        Object enable = props.getOrDefault(Producer.KEY_TRANSMIT_HEADER_ENABLED, "false");
//        if (Boolean.parseBoolean(enable.toString())) {
//            props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Producer.HEADER_INTERCEPT_CLASS);
//        }
        props.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    /**
     * 容器初始化完成后进行添加bean
     *
     * @param beanClass
     */
    private void registerBean(DefaultListableBeanFactory beanFactory, String beanName, Class<?> beanClass,
                              MutablePropertyValues propertyValues) {
        AnnotatedBeanDefinition annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(beanClass);
        ((AnnotatedGenericBeanDefinition) annotatedBeanDefinition).setPropertyValues(propertyValues);
        beanFactory.registerBeanDefinition(beanName, annotatedBeanDefinition);
    }

    /**
     * 属性设置
     */
    private MutablePropertyValues getPropertyValues(Map<String, Object> producerConfig) {
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("kafkaTemplate", kafkaTemplate(producerConfig));
        Object defaultTopic = producerConfig.get(Producer.KEY_DEFAULT_TOPIC.replace("-", "."));
        if (null != defaultTopic) {
            mutablePropertyValues.add("topic", defaultTopic);
        }
        return mutablePropertyValues;
    }

    /**
     * 读取producer配置
     *
     * @param producerName
     * @return
     */
    private Map<String, Object> loadProducerConfigs(String producerName) {
        Set<String> configNames = new HashSet<>();
        configNames.add(Producer.KEY_TRANSMIT_HEADER_ENABLED);
        configNames.addAll(ProducerConfig.configNames());
        configNames.add(Producer.KEY_DEFAULT_TOPIC);

        String prefix = KEY_MULTIPLE + "." + producerName + ".";
        Set<String> keySet = configNames.stream().map(configName -> prefix + configName.replaceAll("[.]", "-")).collect(Collectors.toSet());
        return PropertiesUtil.propertiesToMap(environment, keySet, prefix);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        SpringBeanDefinitionUtil.setBeanRegistry(registry);
    }

    public static class Producer {

        public static final String KEY_TRANSMIT_HEADER_ENABLED = "enable.transmit.header";

        public static final String KEY_DEFAULT_TOPIC = "default-topic";

    }

    public static class Consumer {

        public static final String DEFAULT_AUTO_COMMIT_INTERVAL_MS = "1000";

        public static final String DEFAULT_ENABLE_AUTO_COMMIT = "true";

    }
}