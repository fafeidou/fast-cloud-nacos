package fast.cloud.nacos.example.consumer.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SpringStreamChannel {

    /**
     * 消息消费者
     */
    String CONSUMER = "nanhuiMqSpringStreamConsumer";

    @Input(CONSUMER)
    SubscribableChannel consumer();

    /**
     * 消息生产者
     */
    String PRODUCER = "nanhuiMqSpringStreamProducer";

    @Output(PRODUCER)
    MessageChannel producer();
}
