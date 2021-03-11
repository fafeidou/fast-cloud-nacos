package fast.cloud.nacos.orderservicetcc.feign;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname AccountFeignClient
 * @Description TODO
 * @Date 2020/4/4 21:03
 * @Created by qinfuxiang
 */
@FeignClient(name = "account-service")
public interface AccountFeignClient {

    @GetMapping("/reduce")
    Boolean reduce(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money);
}
