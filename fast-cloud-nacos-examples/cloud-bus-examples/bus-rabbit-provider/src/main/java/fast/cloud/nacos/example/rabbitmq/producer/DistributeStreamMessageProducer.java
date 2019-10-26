package fast.cloud.nacos.example.rabbitmq.producer;

import fast.cloud.nacos.example.rabbitmq.channel.DistributeChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(DistributeChannel.class)
public class DistributeStreamMessageProducer {
    @Resource
    private DistributeChannel channel;


    public void produce(String message) {
        channel.output().send(
                MessageBuilder.withPayload(message)
                .setHeader("version", "1.0")
                .build());

        channel.output().send(
                MessageBuilder.withPayload(message)
                        .setHeader("version", "2.0")
                        .build());
    }
}
