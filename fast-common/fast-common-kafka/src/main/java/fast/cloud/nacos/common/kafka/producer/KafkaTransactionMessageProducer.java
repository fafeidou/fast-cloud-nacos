package fast.cloud.nacos.common.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;

@Component
public class KafkaTransactionMessageProducer implements MessageProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;

    public void setKafkaTemplate(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    @Transactional("kafkaTransactionManager")
    public void sendMessage(Object message) throws RuntimeException {
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional("kafkaTransactionManager")
    public void sendMessage(String topic, Object message) throws RuntimeException {
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional("kafkaTransactionManager")
    public void sendMessage(String topic, String key, Object message) throws RuntimeException {
        try {
            kafkaTemplate.send(topic, key, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destory() {
        try {
            kafkaTemplate.flush();
        } catch (Exception e) {
        }
    }
}
