package fast.cloud.nacos.example.rabbitmq.controller;

import fast.cloud.nacos.example.rabbitmq.producer.*;
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

    @Autowired
    private HandlerExceptionMessageProducer1 handlerExceptionMessageProducer1;

    @Autowired
    private DLQExceptionMessageProducer1 dlqExceptionMessageProducer1;

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
     * @param message
     * @return
     */
    @GetMapping("/distributeMessage")
    public String distributeMessage(@RequestParam String message) {
        log.info("Send: " + message);
        distributeStreamMessageProducer.produce(message);
        return "ok";
    }

    /**
     * 消费异常重试，默认三次
     *
     * @param message
     * @return
     */
    @GetMapping("/handlerException")
    public String handlerException(@RequestParam String message) {
        log.info("Send: " + message);
        handlerExceptionMessageProducer1.produce(message);
        return "ok";
    }

    /**
     * 异常发送到DLQ私信队列
     *
     * @param message
     * @return
     */
    @GetMapping("/dlqException")
    public String dlqException(@RequestParam String message) {
        log.info("Send: " + message);
        dlqExceptionMessageProducer1.produce(message);
        return "ok";
    }

}
