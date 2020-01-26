package fast.cloud.nacos.common.model.beans.context;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * spring 相关处理，主要提供如下两点功能：
 * 1.静态获取spring注册的bean功能
 * 2.发布事件功能
 */
@Slf4j
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.context = applicationContext;
    }

    /**
     * 根据bean名字和类型获取实例
     *
     * @param beanName 名字
     * @param clazz    类型
     * @param <T>      类型T
     * @return 类型实例
     */
    public static <T> Optional<T> getBean(String beanName, Class<T> clazz) {
        if (Strings.isNullOrEmpty(beanName)) {
            log.warn("类名字为空");
            return Optional.empty();
        }
        if (Objects.isNull(clazz)) {
            log.warn("类型为空");
            return Optional.empty();
        }
        if (Objects.isNull(context)) {
            log.warn("spring 加载失败");
            return Optional.empty();
        }
        return Optional.ofNullable(context.getBean(beanName, clazz));
    }

    /**
     * 实际发送事件代码
     *
     * @param event 事件
     * @return 是否成功
     */
    public static boolean publishEvent(ApplicationEvent event) {
        if (Objects.isNull(event)) {
            log.warn("事件为空");
            return false;
        }
        if (Objects.isNull(context)) {
            log.warn("spring 加载失败");
            return false;
        }
        context.publishEvent(event);
        return true;
    }
}
