package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.SpringStreamChannel;
import fast.cloud.nacos.example.consumer.channel.TestDelayChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费逻辑
 */
@Slf4j
@Component
@EnableBinding(TestDelayChannel.class)
public class DelayStreamConsumer {
    @StreamListener(TestDelayChannel.INPUT)
    public void receive(String payload) {
        log.info("Received: " + payload);
    }
}
