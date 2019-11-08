package fast.cloud.nacos.servicehi.controller;

import fast.cloud.nacos.feign.openapi.ServiceHi;
import fast.cloud.nacos.servicehi.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Batman.qin
 * @create 2019-01-23 18:16
 */
@RestController
@RefreshScope
public class HiController implements ServiceHi {
    @Value(value = "${test.name:}")
    private String testName;

    @Value(value = "${common.version:}")
    private String version;

    @Autowired
    private Test test;

    @Value("${server.port}")
    private String port;

    @Override
    public String sayHiFromClientOne(String name) {
        if (name.equals("123")) {
            throw new RuntimeException("非法输入");
        }
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "hi " + name + " ,i am from port:" + port;
    }

    @Override
    public String hello(String name) {
        return "hi " + name + " ,i am from port:" + port;
    }
}
