package fast.cloud.nacos.example.rabbitmq.producer;

import fast.cloud.nacos.example.rabbitmq.channel.TestDelayChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(TestDelayChannel.class)
public class DelayStreamMessage {
    @Resource
    private TestDelayChannel channel;


    public void produce(String message) {
        channel.output().send(MessageBuilder.withPayload(message).setHeader("x-delay", 5000).build());
    }
}
