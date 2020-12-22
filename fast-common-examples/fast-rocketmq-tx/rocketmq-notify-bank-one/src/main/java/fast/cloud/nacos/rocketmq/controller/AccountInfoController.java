package fast.cloud.nacos.rocketmq.controller;

import fast.cloud.nacos.rocketmq.entity.AccountPay;
import fast.cloud.nacos.rocketmq.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@Slf4j
public class AccountInfoController {

    @Autowired
    private AccountInfoService accountInfoService;

    //主动查询充值结果
    @GetMapping(value = "/payresult/{txNo}")
    public AccountPay result(@PathVariable("txNo") String txNo){
        AccountPay accountPay = accountInfoService.queryPayResult(txNo);
        return accountPay;
    }
}
