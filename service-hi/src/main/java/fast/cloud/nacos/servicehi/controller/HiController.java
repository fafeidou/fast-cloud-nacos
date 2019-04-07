package fast.cloud.nacos.servicehi.controller;

import fast.cloud.nacos.feign.openapi.SchedualServiceHi;
import org.springframework.web.bind.annotation.RestController;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2019-01-23 18:16
 */
@RestController
public class HiController implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "hi " + name + " ,i am from port:" + name;
    }
}
