package fast.cloud.nacos.rocketmq.spring;

import fast.cloud.nacos.rocketmq.entity.AccountPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator.
 * 远程调用pay充值系统
 */
@FeignClient(value = "rocketmq-notify-bank2",fallback = PayFallback.class)
public interface PayClient {

    //远程调用充值系统的接口查询充值结果
    @GetMapping(value = "/pay/payresult/{txNo}")
    public AccountPay payresult(@PathVariable("txNo") String txNo);
}
