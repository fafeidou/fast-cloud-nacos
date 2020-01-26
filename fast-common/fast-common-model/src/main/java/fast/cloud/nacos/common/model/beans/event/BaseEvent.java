package fast.cloud.nacos.common.model.beans.event;

import fast.cloud.nacos.common.model.beans.context.SpringApplicationContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * spring event 封装，增加自定义上下午信息透传
 */
@Slf4j
@Getter
public class BaseEvent<T> extends ApplicationEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public BaseEvent(T source) {
        super(source);
    }

    @Override
    public String toString() {
        return this.source.toString();
    }

    /**
     * 发送当前事件
     *
     * @return 是否成功
     */
    public boolean publish() {
        log.info("Event [{}] been published with source: {}", getClass().getSimpleName(), source);
        return SpringApplicationContext.publishEvent(this);
    }
}
