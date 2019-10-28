package fast.cloud.nacos.example.rabbitmq.producer;

import fast.cloud.nacos.example.rabbitmq.channel.DLQExceptionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(DLQExceptionChannel.class)
public class DLQExceptionMessageProducer1 {
    @Resource
    private DLQExceptionChannel channel;


    public void produce(String message) {
        log.info("发送消息：{}", message);
        channel.output().send(MessageBuilder.withPayload(message).build());
    }
}
