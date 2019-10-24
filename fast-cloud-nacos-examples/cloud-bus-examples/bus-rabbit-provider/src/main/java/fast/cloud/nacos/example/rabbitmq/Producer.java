package fast.cloud.nacos.example.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

@EnableBinding(Source.class)
public class Producer {

    @Autowired
    @Output(Source.OUTPUT)
    private MessageChannel channel;

    public void send() {
        channel.send(MessageBuilder.withPayload("22222222222" + UUID.randomUUID().toString()).build());
    }
}