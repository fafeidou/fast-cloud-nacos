package fast.cloud.nacos.example.rabbitmq.controller;

import fast.cloud.nacos.example.rabbitmq.producer.DelayStreamMessage;
import fast.cloud.nacos.example.rabbitmq.producer.DistributeStreamMessageProducer;
import fast.cloud.nacos.example.rabbitmq.producer.SpringStreamMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class HelloController {

    @Resource
    private SpringStreamMessageProducer producer;

    @Autowired
    private DelayStreamMessage delayStreamMessage;

    @Autowired
    private DistributeStreamMessageProducer distributeStreamMessageProducer;

    @GetMapping("/hello")
    public void hello(String message) {
        producer.produce(message);
    }

    /**
     * 测试死信队列
     *
     * @param message
     * @return
     */
    @GetMapping("/sendMessage")
    public String messageWithMQ(@RequestParam String message) {
        log.info("Send: " + message);
        delayStreamMessage.produce(message);
        return "ok";
    }

    /**
     *
     * @param message
     * @return
     */
    @GetMapping("/distributeMessage")
    public String distributeMessage(@RequestParam String message) {
        log.info("Send: " + message);
        distributeStreamMessageProducer.produce(message);
        return "ok";
    }

}
