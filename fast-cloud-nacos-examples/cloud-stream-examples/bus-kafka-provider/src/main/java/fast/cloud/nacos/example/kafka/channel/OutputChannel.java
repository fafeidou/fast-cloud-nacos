package fast.cloud.nacos.example.kafka.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author qfx
 */
public interface OutputChannel {

    @Output("output2")
    MessageChannel output2();
}
