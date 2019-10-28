package fast.cloud.nacos.example.rabbitmq.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface HandlerExceptionChannel {
    String OUTPUT = "handler-exception1-topic-output";
    String INPUT = "handler-exception1-topic-input";

    @Output(OUTPUT)
    MessageChannel output();

    @Input(INPUT)
    SubscribableChannel input();
}
