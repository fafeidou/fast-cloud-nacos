package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.DLQExceptionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(DLQExceptionChannel.class)
public class DLQExceptionConsumer1 {
    @StreamListener(DLQExceptionChannel.INPUT)
    public void consume(String message) {
        log.info("接收消息：{}", message);
        throw new RuntimeException("消费异常");
    }
}
