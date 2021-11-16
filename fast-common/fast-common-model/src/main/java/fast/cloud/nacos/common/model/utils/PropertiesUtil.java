package fast.cloud.nacos.common.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    @SuppressWarnings("unchecked")
    public static <T> T propertiesToClass(Properties properties, Class<T> clazz) {
        try {
            ConfigurationProperties configurationProperties = clazz.getAnnotation(ConfigurationProperties.class);
            if (configurationProperties == null) {
                return null;
            }
            boolean isNull = true;
            Field[] fields = clazz.getDeclaredFields();
            String prefix = configurationProperties.prefix();
            Object object = clazz.newInstance();
            if (null != prefix) {
                for (Field field : fields) {
                    Type typeClz = field.getGenericType();

                    String attr = field.getName();
                    String key = prefix + "." + attr;

                    String value = properties.getProperty(key);
                    if (null == value) {
                        key = prefix + "." + humpToLine(attr);
                        value = properties.getProperty(key);
                    }

                    if (null == value) {
                        continue;
                    }
                    isNull = false;
                    ReflectHelper.setFieldValue(object, field.getName(), value);
                }
            }
            if(isNull) {
                return null;
            } else {
                return (T) object;
            }
        } catch (Exception e) {
            logger.error("exchange properties error", e);
        }
        return null;
    }

    /**
     * 从当前换的配置信息解析到对应的类实例
     * @param environment 当前环境的配置信息
     * @param clazz 配置对应的对象实例Class类型
     * @param <T> 配置对应的对象实例Class类型
     * @return 配置对应的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T propertiesToClass(ConfigurableEnvironment environment, Class<T> clazz) {
        try {
            ConfigurationProperties configurationProperties = clazz.getAnnotation(ConfigurationProperties.class);
            if (configurationProperties == null) {
                return null;
            }
            boolean isNull = true;
            Field[] fields = clazz.getDeclaredFields();
            String prefix = configurationProperties.prefix();
            Object object = clazz.newInstance();
            if (null != prefix) {
                for (Field field : fields) {
                    Type typeClz = field.getGenericType();

                    String attr = field.getName();
                    String key = prefix + "." + attr;

                    String value = environment.getProperty(key);
                    if (null == value) {
                        key = prefix + "." + humpToLine(attr);
                        value = environment.getProperty(key);
                    }

                    if (null == value) {
                        continue;
                    }
                    isNull = false;
                    ReflectHelper.setFieldValue(object, field.getName(), value);
                }
            }

            if(isNull) {
                return null;
            } else {
                return (T) object;
            }
        } catch (Exception e) {
            logger.error("exchange properties error", e);
        }
        return null;
    }

    /**
     * 根据指定的前缀信息从当前环境中获取配置解析成为对应类型的对象实例
     * @param environment 当前环境的配置信息
     * @param clazz 配置对应的对象实例Class类型
     * @param prefix 指定配置前缀信息
     * @param <T> 配置对应的对象实例Class类型
     * @return 配置对应的对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T propertiesToClass(ConfigurableEnvironment environment, Class<T> clazz, String prefix) {
        try {
            boolean isNull = true;
            Field[] fields = clazz.getDeclaredFields();
            Object object = clazz.newInstance();
            if (null != prefix) {
                for (Field field : fields) {
                    Type typeClz = field.getGenericType();

                    String attr = field.getName();
                    String key = prefix + "." + attr;

                    String value = environment.getProperty(key);
                    if (null == value) {
                        key = prefix + "." + humpToLine(attr);
                        value = environment.getProperty(key);
                    }

                    if (null == value) {
                        continue;
                    }
                    isNull = false;
                    ReflectHelper.setFieldValue(object, field.getName(), value);
                }
            }

            if(isNull) {
                return null;
            } else {
                return (T) object;
            }
        } catch (Exception e) {
            logger.error("exchange properties error", e);
        }
        return null;
    }

    /**
     * 根据给定的key集合从环境配置中获取value，并返回k-v集合
     * @param environment 当前环境的配置信息
     * @param keySet 需要获取的配置信息的Key集合
     * @return 配置k-v集合
     */
    public static Map<String, Object> propertiesToMap(ConfigurableEnvironment environment, Set<String> keySet) {
        Map<String, Object> map = new HashMap<>();
        try {

            for (String key : keySet) {

                String value = environment.getProperty(key);
                if (null == value) {
                    key = humpToLine(key);
                    value = environment.getProperty(key);
                }

                if (null == value) {
                    continue;
                } else {
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("exchange properties error", e);
        }
        return map;
    }

    public static Map<String, Object> propertiesToMap(ConfigurableEnvironment environment, Set<String> keySet, String prefix) {
        Map<String, Object> map = new HashMap<>();
        try {

            for (String key : keySet) {

                String value = environment.getProperty(key);
                if (null == value) {
                    key = humpToLine(key);
                    value = environment.getProperty(key);
                }

                if (null == value) {
                    continue;
                } else {
                    map.put(key.replace(prefix, ""), value);
                }
            }
        } catch (Exception e) {
            logger.error("exchange properties error", e);
        }
        return map;
    }

    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "-$0").toLowerCase();
    }
}
