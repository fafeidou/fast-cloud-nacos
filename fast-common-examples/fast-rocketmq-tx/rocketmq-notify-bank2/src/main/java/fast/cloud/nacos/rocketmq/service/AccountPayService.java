package fast.cloud.nacos.rocketmq.service;


import fast.cloud.nacos.rocketmq.entity.AccountPay;

/**
 * Created by Administrator.
 */
public interface AccountPayService {

    //充值
    public AccountPay insertAccountPay(AccountPay accountPay);
    //查询充值结果
    public AccountPay getAccountPay(String txNo);
}
