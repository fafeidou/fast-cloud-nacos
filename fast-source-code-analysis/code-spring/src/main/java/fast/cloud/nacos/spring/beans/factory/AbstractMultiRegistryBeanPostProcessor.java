package fast.cloud.nacos.spring.beans.factory;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * 批量注册自定义配置
 * 默认实现为单个bean注册，
 * 若需要多个bean注册，重写 #doBeanRegister 并且在 #configBeanBeforeInitialize 实现配置参数绑定
 *
 * @see #doBeanRegister(String, Object)
 * @see #configBeanBeforeInitialize(String, String, Object, Object)
 */
public abstract class AbstractMultiRegistryBeanPostProcessor<C, B> implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanFactoryAware, BeanPostProcessor {

    /**
     * 单个配置时，存储到多配置的固定前缀
     */
    protected static final String DEFAULT_KEY = "default";

    /**
     * 根据前缀读取到的配置bean
     */
    protected Map<String, C> configMap = new HashMap<>();

    /**
     * 所有通过该类注册的bean名字对应的bean数据的集合
     */
    protected Map<String, Object> beanMap = new HashMap<>();

    /**
     * bean注册的时候缓存对应的名字，在配置bean时，获取当前bean的配置前缀
     * 后去使用remove，当beanPrefixMap.size() == 0时，说明bean已经全部完成配置，可以清理当前类信息
     */
    private Map<String, String> beanPrefixMap = new HashMap<>();

    /**
     * 配置bean的类型
     */
    private Class<C> configClazz;

    /**
     * 单个配置的bean的类型
     */
    private Class<B> beanClazz;

    @Getter
    private BeanDefinitionRegistry registry;

    /**
     * 禁用无参构造方法，必须传递配置的bean类型和注册的bean类型
     */
    private AbstractMultiRegistryBeanPostProcessor() {
    }

    public Object getRegisteredBean(String beanName) {
        return beanMap.get(beanName);
    }

    public AbstractMultiRegistryBeanPostProcessor(Class<C> configClazz, Class<B> beanClazz) {
        this.configClazz = configClazz;
        this.beanClazz = beanClazz;
    }

    protected static String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    protected static String lowwerCaseFirst(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (StringUtils.isEmpty(configMap)) {
            return;
        }

        this.registry = registry;

        for (Map.Entry<String, C> configEntry : configMap.entrySet()) {
            String prefix = configEntry.getKey();
            C config = configEntry.getValue();

            doBeanRegister(prefix, config);
        }
    }

    /**
     * bean注册，默认实现未单个bean使用生成的名字进行注册，
     * bean注册有其它需求时，重写此方法，根据需求调用registryBean完成注册
     * 注意多个依赖bean注册时，注册时指定依赖关系
     *
     * @param prefix 配置前缀
     * @see #configBeanBeforeInitialize(String, String, Object, Object)
     * @see #registryBean
     * @see #registryBean(String, String, Class, Consumer) 通过consumer指定setDependsOn
     */
    public void doBeanRegister(String prefix, C config) {
        registryBean(prefix);
    }

    /**
     * 根据配置创建所需注册的bean，用于注册后
     *
     * @param prefix   当前需要bean和配置的前缀
     * @param beanName 当前需要配置的bean的名字
     * @param bean     需要配置的bean
     * @param config   从外部化读取到的配置
     * @return 根据当前配置已完成配置的bean
     */
    public abstract Object configBeanBeforeInitialize(String prefix, String beanName, Object bean, C config);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private AbstractBeanDefinition getBeanDefinition(Class<?> beanType) {
        BeanDefinitionBuilder builder = rootBeanDefinition(beanType);

        return builder.getBeanDefinition();
    }

    /**
     * 根据配置的前缀和当前类型进行注册
     *
     * @param prefix 前缀
     */
    public void registryBean(String prefix) {
        register(prefix, null, beanClazz, null, null);
    }

    /**
     * 根据配置的前缀和当前类型进行注册，需要指定初始化或者销毁方式bean注册
     *
     * @param prefix            前缀
     * @param initMethodName    初始化方法
     * @param destroyMethodName 销毁方法
     */
    public void registryBean(String prefix, String initMethodName, String destroyMethodName) {
        register(prefix, null, beanClazz, initMethodName, destroyMethodName);
    }

    /**
     * 使用配置的前缀，且指定beanName名字，当前类型进行注册
     *
     * @param prefix   前缀
     * @param beanName 指定生成bean的名字，而不是使用类名转换
     */
    public void registryBean(String prefix, String beanName) {
        register(prefix, beanName, beanClazz, null, null);
    }

    /**
     * 使用配置的前缀，指定bean类型进行注册
     *
     * @param prefix   前缀
     * @param beanType 当前bean类型
     */
    public void registryBean(String prefix, Class<?> beanType) {
        register(prefix, null, beanType, null, null);
    }

    /**
     * 使用配置的前缀，且指定beanName名字，指定bean类型进行注册
     *
     * @param prefix   前缀
     * @param beanName 指定生成bean的名字，而不是使用类名转换
     * @param beanType 当前bean类型
     */
    public void registryBean(String prefix, String beanName, Class<?> beanType) {
        register(prefix, beanName, beanType, null, null);
    }

    /**
     * 使用配置的前缀，指定bean类型，需要指定初始化或者销毁方式bean注册
     *
     * @param prefix            前缀
     * @param beanType          当前bean类型
     * @param initMethodName    初始化方法
     * @param destroyMethodName 销毁方法
     */
    public void registryBean(String prefix, Class<?> beanType, String initMethodName, String destroyMethodName) {
        register(prefix, null, beanType, initMethodName, destroyMethodName);
    }

    /**
     * 需要指定初始化或者销毁方式bean注册
     *
     * @param prefix            前缀
     * @param beanName          指定生成bean的名字，而不是使用类名转换
     * @param beanType          当前bean类型
     * @param initMethodName    初始化方法
     * @param destroyMethodName 销毁方法
     */
    public void registryBean(String prefix, String beanName, Class<?> beanType, String initMethodName, String destroyMethodName) {
        register(prefix, beanName, beanType, initMethodName, destroyMethodName);
    }

    public void registryBean(String prefix, String beanName, Class<?> beanType, Consumer<AbstractBeanDefinition> beanDefinitionConsumer) {
        register(prefix, beanName, beanType, beanDefinitionConsumer);
    }

    public String getBeanName(String prefix, String beanName, Class<?> beanType) {
        if (StringUtils.isEmpty(beanName)) {
            beanName = generateBeanName(beanType);
        }
        if (DEFAULT_KEY.equals(prefix)) {
            return lowwerCaseFirst(beanName);
        }

        return prefix + upperCaseFirst(beanName);
    }

    /**
     * 是否为默认配置，单个bean配置时，此方法返回为true，否则为false
     *
     * @param configPrefix 当前配置项前缀
     * @return
     */
    public boolean isDefault(String configPrefix) {
        return DEFAULT_KEY.equals(configPrefix);
    }

    private void register(String prefix, String beanName, Class<?> beanType, Consumer<AbstractBeanDefinition> beanDefinitionConsumer) {
        beanName = getBeanName(prefix, beanName, beanType);

        AbstractBeanDefinition beanDefinition = getBeanDefinition(beanType);

        // 默认标记primary
        if (isDefault(prefix)) {
            beanDefinition.setPrimary(true);
        }

        beanDefinitionConsumer.accept(beanDefinition);

        // 记录是自己注册的，非自己注册的，直接在Before返回，不做处理
        beanPrefixMap.put(beanName, prefix);

        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private void register(String prefix, String beanName, Class<?> beanType, String initMethodName, String destroyMethodName) {
        register(prefix, beanName, beanType, beanDefinition -> {
            if (!StringUtils.isEmpty(initMethodName)) {
                beanDefinition.setInitMethodName(initMethodName);
            }

            if (!StringUtils.isEmpty(destroyMethodName)) {
                beanDefinition.setDestroyMethodName(destroyMethodName);
            }
        });
    }

    private String generateBeanName(Class<?> beanType) {
        return beanType.getSimpleName();
    }

    @Override
    public void setEnvironment(Environment environment) {
        if (Objects.nonNull(getSingleKey())) {
            BindResult<C> defaultConfigBindResult = Binder.get(environment)
                    .bind(getSingleKey(), Bindable.of(configClazz));

            if (defaultConfigBindResult.isBound()) {
                configMap.put(DEFAULT_KEY, defaultConfigBindResult.get());
            }

        }
        BindResult<Map<String, C>> bindResult = Binder.get(environment)
                .bind(getMultiKey(), Bindable.mapOf(String.class, configClazz));

        if (!bindResult.isBound() && StringUtils.isEmpty(configMap)) {
            return;
        }

        if (bindResult.isBound()) {
            configMap.putAll(bindResult.get());
        }

        initDefaultConfiguration();
    }

    private void initDefaultConfiguration() {
        for (Map.Entry<String, C> entry : configMap.entrySet()) {
            initSingleDefaultConfiguration(entry.getKey(), entry.getValue(), configMap.get(DEFAULT_KEY));
        }
    }

    /**
     * 单个配置项的默认值配置处理
     *
     * @param prefix        当前配置项前缀
     * @param config        当前配置项
     * @param defaultConfig 默认配置项
     */
    public void initSingleDefaultConfiguration(String prefix, C config, C defaultConfig) {

    }

    /**
     * 单个值配置时的key
     *
     * @return 单个key配置的前缀
     */
    public String getSingleKey() {
        return null;
    }

    /**
     * 多个值配置时的key
     *
     * @return 批量配置前缀
     */
    public abstract String getMultiKey();

    /**
     * 通过指定方法注册，spring已创建，但未初始化的bean处理
     *
     * @param bean     已创建的bean
     * @param beanName 对应bean的名字
     * @return 设置配置内容之后的bean
     * @throws BeansException bean配置时抛出的异常
     * @see #registryBean
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 存在说明需要进行处理，不存在，可能已经配置过，不需要再进行处理了
        if (beanPrefixMap.containsKey(beanName)) {
            String prefix = beanPrefixMap.remove(beanName);
            bean = configBeanBeforeInitialize(prefix, beanName, bean, configMap.get(prefix));

            if (Objects.isNull(beanMap.get(beanName))) {
                beanMap.put(beanName, bean);
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof AbstractBeanFactory) {
            ((AbstractBeanFactory) beanFactory).addBeanPostProcessor(this);
        }
    }
}

