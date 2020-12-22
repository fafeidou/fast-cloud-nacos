package fast.cloud.nacos.rocketmq.service;


import fast.cloud.nacos.rocketmq.entity.AccountPay;
import fast.cloud.nacos.rocketmq.model.AccountChangeEvent;

/**
 * Created by Administrator.
 */
public interface AccountInfoService {

    //更新账户金额
    public void updateAccountBalance(AccountChangeEvent accountChange);

    //查询充值结果（远程调用）
    public AccountPay queryPayResult(String tx_no);

}
