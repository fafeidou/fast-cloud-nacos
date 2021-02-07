package fast.cloud.nacos.example.kafka.consumer;

import fast.cloud.nacos.example.kafka.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * Kafka-Binder
 * <spring-cloud-stream-binder-kafka>
 */
@Slf4j
@Service
@EnableBinding(Sink.class)
public class CustomMessageLegacyListener {

    @StreamListener(Sink.INPUT)
    public void process(Message<CustomMessage> message) {
        log.info("process() " + message.getPayload().getData());

        /**
         * do something
         */

        throw new RuntimeException(); // exception -> to dead-letter-topic
    }
}
