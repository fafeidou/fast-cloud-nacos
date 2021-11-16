package fast.cloud.nacos.fastbootkafka.controller;

import fast.cloud.nacos.fastbootkafka.domain.TopicATest;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author qfx
 */
@RestController
public class KafkaDemoController {

    @Autowired
    AdminClient adminClient;

    /**
     * 创建topic
     */
    @RequestMapping("createTopic")
    public String createTopic(String topicName) {
        NewTopic topic = new NewTopic(topicName, 2, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        return topicName;
    }

    /**
     * 查询topic
     */
    @RequestMapping("queryTopic")
    public String queryTopic(String topicName) {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topicName));
        StringBuffer sb = new StringBuffer("topic信息:");
        try {
            result.all().get().forEach((k, v) -> sb.append("key").append(k).append(";v:").append(v));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 删除topic
     */
    @RequestMapping("deleteTopic")
    public String deleteTopic(String topicName) {
        adminClient.deleteTopics(Arrays.asList(topicName));
        return topicName;
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    @Qualifier(value = "sourceAKafkaTemplate")
    private KafkaTemplate<String, Object> sourceAKafkaTemplate;

    @Autowired
    @Qualifier(value = "sourceBKafkaTemplate")
    private KafkaTemplate<String, Object> sourceBKafkaTemplate;

    /**
     * 发送文字消息
     */
    @RequestMapping("sendStr")
    public String sendStr(String message) {
        kafkaTemplate.send("kafka-topic1", message);
        return message;
    }

    @RequestMapping("sendStr2")
    public String sendStr2(String message) {
        kafkaTemplate.send("kafka-topic2", message);
        return message;
    }

    @RequestMapping("sendStr3")
    public String sendStr3(String message) {
        kafkaTemplate.send("kafka-topic3", message);
        return message;
    }

    @RequestMapping("sendSourceA")
    public String sendSourceA(String message) {
        TopicATest topicATest = new TopicATest();
        topicATest.setPayload(message);
        sourceAKafkaTemplate.send("topicA", topicATest);
        return message;
    }

    @RequestMapping("sendSourceB")
    public String sendSourceB(String message) {
        sourceBKafkaTemplate.send("topicC", message);
        return message;
    }
}
