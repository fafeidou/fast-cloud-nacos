package fast.cloud.nacos.common.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class KafkaMessageProducer implements MessageProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;

    public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public KafkaTemplate<String, Object> getKafkaTemplate() {
        return kafkaTemplate;
    }

    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void sendMessage(Object message) throws RuntimeException {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void sendMessage(String topic, Object message) throws RuntimeException {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void sendMessage(String topic, String key, Object message) throws RuntimeException {
        kafkaTemplate.send(topic, key, message);
    }

    @PreDestroy
    public void destory() {
        try {
            kafkaTemplate.flush();
        } catch (Exception e) {
        }
    }
}
