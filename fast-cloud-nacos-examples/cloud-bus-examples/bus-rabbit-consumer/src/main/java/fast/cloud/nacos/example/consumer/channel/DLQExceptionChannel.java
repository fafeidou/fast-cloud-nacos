package fast.cloud.nacos.example.consumer.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DLQExceptionChannel {
    String OUTPUT = "handler-dlq-topic-output";
    String INPUT = "handler-dlq-topic-input";

    @Output(OUTPUT)
    MessageChannel output();

    @Input(INPUT)
    SubscribableChannel input();
}
