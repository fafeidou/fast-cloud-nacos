package fast.cloud.nacos.securityapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Batman.qin
 */
@FeignClient(value = "security-provider")
public interface ServiceHi {
    String ROOT = "security";

    @RequestMapping(value = ROOT + "/hello", method = RequestMethod.GET)
    String hello();

    @RequestMapping(value = ROOT + "/hello2", method = RequestMethod.GET)
//    @ApiOperation(value = "sayHiFromClientOne", notes = "sayHiFromClientOne")
    String hello2();
}
