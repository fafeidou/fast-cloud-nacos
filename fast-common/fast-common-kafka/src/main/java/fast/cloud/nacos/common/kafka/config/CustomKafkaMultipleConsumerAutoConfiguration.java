package fast.cloud.nacos.common.kafka.config;

import fast.cloud.nacos.common.model.utils.EnvironmentUtil;
import fast.cloud.nacos.common.model.utils.SpringBeanDefinitionUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Listener;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 多源Consumer配置类
 */
@Configuration
public class CustomKafkaMultipleConsumerAutoConfiguration implements BeanFactoryPostProcessor {

    private String KEY_MULTIPLE_CONSUMER_LISTENER = "spring.kafka.multiple.consumer.%s.listener";
    private String KEY_MULTIPLE_CONSUMER_PROPERTIES = "spring.kafka.multiple.consumer.%s.properties";

    private String KEY_SOURCES = "spring.kafka.multiple.consumer.sources";

    private String KEY_MULTIPLE = "spring.kafka.multiple.consumer";

    private ConfigurableEnvironment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.environment = EnvironmentUtil.getEnvironment();
        // 注册消费监听容器类
        registerListenerContainerFactory();
    }

    private void registerListenerContainerFactory() {
        // 获取消费者名称配置 多个以，分割
        String sources = environment.getProperty(KEY_SOURCES);
        if (StringUtils.isEmpty(sources)) {
            return;
        }
        Arrays.asList(sources.trim().split(",")).forEach(consumerName -> {
            // 创建监听容器工厂
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory =
                    createKafkaListenerContainerFactory(loadConsumerConfigs(consumerName), consumerName);
            // 注册到容器
            SpringBeanDefinitionUtil.getBeanFactory().registerSingleton(consumerName, containerFactory);
        });
    }

    /**
     * 创建containerFactory
     *
     * @param consumerProperties
     * @param consumerName
     * @return
     */
    private ConcurrentKafkaListenerContainerFactory<String, String> createKafkaListenerContainerFactory(
            Map<String, Object> consumerProperties, String consumerName) {
        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        configureContainerFactory(factory, consumerName);
        return factory;
    }

    /**
     * 加载生产者配置
     *
     * @param consumerName
     * @return
     */
    private Map<String, Object> loadConsumerConfigs(String consumerName) {
        Map<String, Object> consumerConfigs = new HashMap<>();
        String configKeyPrefix = KEY_MULTIPLE + "." + consumerName + ".";
        ConsumerConfig.configNames().forEach(configName -> {
            String configValue = environment.
                    getProperty(configKeyPrefix + configName.replaceAll("[.]", "-"));
            if (!StringUtils.isEmpty(configValue)) {
                consumerConfigs.put(configName, configValue);
            }
        });
        //配置properties 信息
        BindResult<Properties> bindResult = Binder.get(environment)
                .bind(String.format(KEY_MULTIPLE_CONSUMER_PROPERTIES, humpToLine(consumerName)), Properties.class);

        if (bindResult.isBound()) {
            consumerConfigs.putAll((Map) bindResult.get());
        }
        consumerConfigs.putIfAbsent(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
                Consumer.DEFAULT_AUTO_COMMIT_INTERVAL_MS);
        consumerConfigs.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                Consumer.DEFAULT_ENABLE_AUTO_COMMIT);
        consumerConfigs.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        consumerConfigs.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        return consumerConfigs;
    }

    /**
     * 配置containerFactory
     *
     * @param containerFactory
     * @param consumerName
     */
    private void configureContainerFactory(ConcurrentKafkaListenerContainerFactory<String, String> containerFactory,
                                           String consumerName) {
        BindResult<Listener> result = Binder.get(environment).bind(
                String.format(KEY_MULTIPLE_CONSUMER_LISTENER, humpToLine(consumerName)), Listener.class);
        if (result.isBound()) {
            ContainerProperties container = containerFactory.getContainerProperties();
            Listener properties = result.get();
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(properties::getAckMode).to(container::setAckMode);
            map.from(properties::getClientId).to(container::setClientId);
            map.from(properties::getAckCount).to(container::setAckCount);
            map.from(properties::getAckTime).as(Duration::toMillis).to(container::setAckTime);
            map.from(properties::getNoPollThreshold).to(container::setNoPollThreshold);
            map.from(properties::getIdleEventInterval).as(Duration::toMillis).to(container::setIdleEventInterval);
            map.from(properties::getPollTimeout).as(Duration::toMillis).to(container::setPollTimeout);
            map.from(properties::getMonitorInterval).as(Duration::getSeconds).as(Number::intValue)
                    .to(container::setMonitorInterval);
            map.from(properties::getLogContainerConfig).to(container::setLogContainerConfig);
//            map.from(properties::isMissingTopicsFatal).to(container::setMissingTopicsFatal);
        }
    }

    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "-$0").toLowerCase();
    }

    public static class Consumer {

        public static final String DEFAULT_AUTO_COMMIT_INTERVAL_MS = "1000";

        public static final String DEFAULT_ENABLE_AUTO_COMMIT = "true";

    }
}
