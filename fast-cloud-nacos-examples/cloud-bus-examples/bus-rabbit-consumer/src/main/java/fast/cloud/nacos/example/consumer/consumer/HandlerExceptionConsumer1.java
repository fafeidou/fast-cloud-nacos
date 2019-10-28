package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.HandlerExceptionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(HandlerExceptionChannel.class)
public class HandlerExceptionConsumer1 {
    @StreamListener(HandlerExceptionChannel.INPUT)
    public void consume(String message) {
        log.info("接收消息：{}", message);
        throw new RuntimeException("消费异常");
    }

    /**
     * 消息消费失败的降级处理逻辑
     * destination + group
     * @param message
     */
    @ServiceActivator(inputChannel = "handler-exception1-topic.handler-exception1.errors")
    public void error(Message<?> message) {
        log.info("Message consumer failed, call fallback!");
    }
}
