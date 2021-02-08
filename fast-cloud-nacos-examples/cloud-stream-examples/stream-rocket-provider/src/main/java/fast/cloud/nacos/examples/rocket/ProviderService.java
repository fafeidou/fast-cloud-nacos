package fast.cloud.nacos.examples.rocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {
    @Autowired
    private MessageChannel output;

    public void send(String message) {
        output.send(MessageBuilder.withPayload(message).build());
    }
}
