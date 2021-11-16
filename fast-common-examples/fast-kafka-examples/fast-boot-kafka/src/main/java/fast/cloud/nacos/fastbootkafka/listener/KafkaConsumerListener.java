package fast.cloud.nacos.fastbootkafka.listener;

import fast.cloud.nacos.fastbootkafka.domain.TopicATest;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author qfx
 */

@Component
@Log4j2
public class KafkaConsumerListener {

    @Autowired
    private ConsumerFactory consumerFactory;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Bean
    public ConcurrentKafkaListenerContainerFactory containerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(AckMode.MANUAL);
        // 最大重试次数3次
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate), 3));
        return factory;
    }

    @KafkaListener(topics = "kafka-topic1")
    public void onMessage1(String message) {
        System.out.println(message);
        log.info("kafka-topic1接收结果:{}", message);
    }

    /**
     * 测试死信消息
     * @param acknowledgment 手动ack
     */
    @KafkaListener(topics = "kafka-topic2", containerFactory = "containerFactory", groupId = "testGroup")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Exception {
        Optional kafkaMessage = Optional.ofNullable(record.value());

        if (!kafkaMessage.isPresent()) {
            throw new Exception("监听到的消息为空值");
        }

        log.info("topicID: " + record.topic());
        log.info("recordValue: " + record.value());

        try {
            /*业务逻辑*/
            throw new RuntimeException("消息异常，进入死信队列...");
        } catch (Exception e) {
            acknowledgment.acknowledge();
            throw new Exception(e);
        }
    }

    @KafkaListener(id = "testGroup", topics = "kafka-topic2.DLT", groupId = "testGroup")
    public void dltListen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exception,
                          @Header(KafkaHeaders.DLT_EXCEPTION_STACKTRACE) String stacktrace) {
        log.info("Received from DLT: " + record.value());
        acknowledgment.acknowledge();
    }

    /**
     * 测试单个源的消费
     *
     * @param message
     */
    @KafkaListener(topics = "kafka-topic3")
    public void onMessage3(String message) {
        System.out.println(message);
        log.info("kafka-topic3接收结果:{}", message);
    }

    /**
     * 测试多个源消费
     *
     * @param acknowledgment 手动ack
     */
    @KafkaListener(topics = "topicA", containerFactory = "sourceA")
    public void onMessageTopicA(ConsumerRecord<Object, TopicATest> record, Acknowledgment acknowledgment) {
        try {
            System.out.println(record.value());
            log.info("topicA接收结果:{}", record.value());
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.warn("Consume topicA Failed", ex);
        }
    }


    /**
     * 测试多个源消费
     *
     * @param acknowledgment 手动ack
     */
    @KafkaListener(topics = "topicC", containerFactory = "sourceB")
    public void onMessageTopicC(ConsumerRecord<Object, String> record, Acknowledgment acknowledgment) {
        try {
            System.out.println(record.value());
            log.info("topicC接收结果:{}", record.value());
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.warn("Consume topicC Failed", ex);
        }

    }
}


