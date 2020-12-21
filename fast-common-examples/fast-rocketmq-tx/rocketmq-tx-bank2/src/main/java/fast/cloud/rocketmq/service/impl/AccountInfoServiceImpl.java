package fast.cloud.rocketmq.service.impl;

import fast.cloud.rocketmq.dao.AccountInfoDao;
import fast.cloud.rocketmq.model.AccountChangeEvent;
import fast.cloud.rocketmq.service.AccountInfoService;
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

    //更新账户，增加金额
    @Override
    @Transactional
    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        log.info("bank2更新本地账号，账号：{},金额：{}",accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo())>0){

            return ;
        }
        //增加金额
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        //添加事务记录，用于幂等
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        if(accountChangeEvent.getAmount() == 4){
            throw new RuntimeException("人为制造异常");
        }
    }
}
