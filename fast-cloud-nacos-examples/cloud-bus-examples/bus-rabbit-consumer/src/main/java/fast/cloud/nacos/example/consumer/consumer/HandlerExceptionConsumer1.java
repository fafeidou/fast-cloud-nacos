package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.HandlerExceptionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
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
}
