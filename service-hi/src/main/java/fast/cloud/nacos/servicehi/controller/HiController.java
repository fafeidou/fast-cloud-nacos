package fast.cloud.nacos.servicehi.controller;

import fast.cloud.nacos.feign.openapi.SchedualServiceHi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2019-01-23 18:16
 */
@RestController
@RefreshScope
public class HiController implements SchedualServiceHi {
    @Value(value = "${test.name:}")
    private String testName;

    @Value(value = "${common.version:}")
    private String version;

    @Override
    public String sayHiFromClientOne(String name) {
        System.out.println(testName);
        System.out.println(version);
        return "hi " + name + " ,i am from port:" + name;
    }
}
