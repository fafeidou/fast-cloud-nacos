package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.SpringStreamChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@Slf4j
@EnableBinding(SpringStreamChannel.class)
public class SpringStreamMessageConsumer {

    @StreamListener(SpringStreamChannel.CONSUMER)
    public void consume(String message) {
        log.info("接收消息：{}", message);
    }
}
