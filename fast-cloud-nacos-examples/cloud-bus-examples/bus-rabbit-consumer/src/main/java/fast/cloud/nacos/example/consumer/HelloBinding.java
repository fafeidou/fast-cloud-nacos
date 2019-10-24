package fast.cloud.nacos.example.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface HelloBinding {
    String GREETING = "greetingChannel";

    @Input(GREETING)
    SubscribableChannel greeting();
}
