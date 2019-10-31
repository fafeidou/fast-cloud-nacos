package com.fast.nacos.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
@RestController
public class ServiceProducerApplication {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static void main(String[] args) {
        SpringApplication.run(ServiceProducerApplication.class, args);
    }

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String trace() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        logger.info("trace product");
        return "dfdfdsfdf";
    }
}
