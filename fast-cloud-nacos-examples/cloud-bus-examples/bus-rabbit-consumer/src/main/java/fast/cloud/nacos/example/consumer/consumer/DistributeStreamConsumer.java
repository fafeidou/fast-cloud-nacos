package fast.cloud.nacos.example.consumer.consumer;

import fast.cloud.nacos.example.consumer.channel.DistributeChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 消息消费逻辑
 */
@Slf4j
@Component
@EnableBinding(DistributeChannel.class)
public class DistributeStreamConsumer {

    @StreamListener(value = DistributeChannel.INPUT, condition = "headers['version']=='1.0'")
    public void receiveV1(String payload, @Header("version") String version) {
        log.info("Received v1 : " + payload + ", " + version);
    }

    @StreamListener(value = DistributeChannel.INPUT, condition = "headers['version']=='2.0'")
    public void receiveV2(String payload, @Header("version") String version) {
        log.info("Received v2 : " + payload + ", " + version);
    }

    @StreamListener(value = DistributeChannel.INPUT)
    public void receiveV3(String payload, @Header("version") String version) {
        log.info("Received v3 : " + payload + ", " + version);
    }
}
