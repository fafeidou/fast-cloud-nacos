package fast.cloud.nacos.tenant.aop;

import fast.cloud.nacos.common.model.beans.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 事件切面
 */
@Slf4j
@Aspect
@Component
public class EventHandlerAop {

    @Before(value = "execution(* fast.cloud.nacos.tenant.event.*Handler.*(..))")
    public void beforeEventHandling(JoinPoint point) {
        String eventName = null;
        Object[] params = point.getArgs();
        if (!Objects.isNull(params) && params.length > 0) {
            if (params[0] instanceof BaseEvent) {
                // 设置上下文
                BaseEvent baseEvent = (BaseEvent) params[0];

                eventName = baseEvent.getClass().getSimpleName();
            }
        } else {

            for (Object p : params) {
                if (p.getClass().getSimpleName().endsWith("Event")) {
                    eventName = p.getClass().getSimpleName();
                    break;
                }
            }
            // 当前获取的是执行的方法名
            if (Objects.isNull(eventName)) {
                eventName = point.getSignature().getName();
            }
        }

        log.info("{} is processing with param: {}", eventName, params);
    }
}
