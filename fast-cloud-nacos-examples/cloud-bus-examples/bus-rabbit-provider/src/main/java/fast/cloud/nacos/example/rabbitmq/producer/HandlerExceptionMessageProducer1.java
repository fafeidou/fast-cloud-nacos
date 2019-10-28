package fast.cloud.nacos.example.rabbitmq.producer;

import fast.cloud.nacos.example.rabbitmq.channel.HandlerExceptionChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(HandlerExceptionChannel.class)
public class HandlerExceptionMessageProducer1 {
    @Resource
    private HandlerExceptionChannel channel;


    public void produce(String message) {
        log.info("发送消息：{}", message);
        channel.output().send(MessageBuilder.withPayload(message).build());
    }
}
