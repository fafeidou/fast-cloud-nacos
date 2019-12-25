package fast.cloud.nacos.securityapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Batman.qin
 */
@FeignClient(value = "security-provider")
public interface ServiceHi {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello();

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
//    @ApiOperation(value = "sayHiFromClientOne", notes = "sayHiFromClientOne")
    public String hello2();
}
