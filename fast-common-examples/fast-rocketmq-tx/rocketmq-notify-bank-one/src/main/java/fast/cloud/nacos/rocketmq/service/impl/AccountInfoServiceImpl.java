package fast.cloud.nacos.rocketmq.service.impl;

import fast.cloud.nacos.rocketmq.dao.AccountInfoDao;
import fast.cloud.nacos.rocketmq.entity.AccountPay;
import fast.cloud.nacos.rocketmq.model.AccountChangeEvent;
import fast.cloud.nacos.rocketmq.service.AccountInfoService;
import fast.cloud.nacos.rocketmq.spring.PayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    PayClient payClient;

    //更新账户金额
    @Override
    @Transactional
    public void updateAccountBalance(AccountChangeEvent accountChange) {
        //幂等校验
        if(accountInfoDao.isExistTx(accountChange.getTxNo())>0){
            return ;
        }
        int i = accountInfoDao.updateAccountBalance(accountChange.getAccountNo(), accountChange.getAmount());
        //插入事务记录，用于幂等控制
        accountInfoDao.addTx(accountChange.getTxNo());
    }

    //远程调用查询充值结果
    @Override
    public AccountPay queryPayResult(String tx_no) {

        //远程调用
        AccountPay payresult = payClient.payresult(tx_no);
        if("success".equals(payresult.getResult())){
            //更新账户金额
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent();
            accountChangeEvent.setAccountNo(payresult.getAccountNo());//账号
            accountChangeEvent.setAmount(payresult.getPayAmount());//金额
            accountChangeEvent.setTxNo(payresult.getId());//充值事务号
            updateAccountBalance(accountChangeEvent);
        }
        return payresult;
    }
}
