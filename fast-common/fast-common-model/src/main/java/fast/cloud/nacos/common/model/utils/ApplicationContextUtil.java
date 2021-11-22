package fast.cloud.nacos.common.model.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author qinfuxiang
 */
public class ApplicationContextUtil {

    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public void destroy() throws Exception {
        applicationContext = null;
    }

    /**
     * 获取applicationContext
     *
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据Bean名称获取实例
     *
     * @param name
     *            Bean注册名称
     *
     * @return bean实例
     *
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据Bean类获取实例
     *
     * @param requiredType
     *            Bean注册类
     *
     * @return bean实例
     *
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        if (null != applicationContext) {
            return applicationContext.getBeansOfType(type);
        } else {
            return null;
        }
    }

    public static <T> T getDefaultBeanOfType(Class<T> type) {
        if (null != applicationContext) {
            return (T) applicationContext.getBeansOfType(type).values().iterator().next();
        } else {
            return null;
        }
    }

}
