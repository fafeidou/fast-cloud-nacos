package fast.cloud.nacos.feign.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2018-11-23 19:36
 */
//@Api("ServiceHi")
@FeignClient(value = "service-hi", fallback = ServiceHiFallback.class/*fallbackFactory = ServiceHiFactory.class*/)
public interface ServiceHi {
    Logger logger = LoggerFactory.getLogger(ServiceHi.class);

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
//    @ApiOperation(value = "sayHiFromClientOne", notes = "sayHiFromClientOne")
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    @ApiOperation(value = "sayHiFromClientOne", notes = "sayHiFromClientOne")
    String hello(@RequestParam(value = "name") String name);
}
