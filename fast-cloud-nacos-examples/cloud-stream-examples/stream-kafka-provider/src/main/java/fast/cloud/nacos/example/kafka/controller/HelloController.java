package fast.cloud.nacos.example.kafka.controller;

import fast.cloud.nacos.example.kafka.model.CustomMessage;
import fast.cloud.nacos.example.kafka.producer.DlqMessageProducer;
import fast.cloud.nacos.example.kafka.producer.SpringStreamMessageProducer;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Resource
    private SpringStreamMessageProducer producer;

    @Resource
    private DlqMessageProducer dlqMessageProducer;

    @GetMapping("/hello")
    public void hello(String message) {
        producer.produce(message);
    }

    @GetMapping("/produce")
    public void produce() {
        dlqMessageProducer.produce(CustomMessage.builder()
            .data("test")
            .count(1).build());
    }

}
