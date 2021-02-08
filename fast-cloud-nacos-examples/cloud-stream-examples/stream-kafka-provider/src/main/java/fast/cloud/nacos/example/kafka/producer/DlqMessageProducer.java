package fast.cloud.nacos.example.kafka.producer;

import fast.cloud.nacos.example.kafka.channel.OutputChannel;
import fast.cloud.nacos.example.kafka.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@Slf4j
@EnableBinding(OutputChannel.class)
public class DlqMessageProducer {

    @Autowired
    @Qualifier(value = "output2")
    private MessageChannel channel;


    public void produce(CustomMessage message) {
        channel.send(MessageBuilder.withPayload(message).build());
    }

}
