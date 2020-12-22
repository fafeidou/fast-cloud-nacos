package fast.cloud.nacos.rocketmq.message;

import com.alibaba.fastjson.JSON;
import fast.cloud.nacos.rocketmq.entity.AccountPay;
import fast.cloud.nacos.rocketmq.model.AccountChangeEvent;
import fast.cloud.nacos.rocketmq.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
@Slf4j
@RocketMQMessageListener(topic = "topic_notifymsg",consumerGroup = "consumer_group_notifymsg_bank1")
public class NotifyMsgListener implements RocketMQListener<AccountPay> {

    @Autowired
    AccountInfoService accountInfoService;

    //接收消息
    @Override
    public void onMessage(AccountPay accountPay) {
        log.info("接收到消息：{}", JSON.toJSONString(accountPay));
        if("success".equals(accountPay.getResult())){
            //更新账户金额
            AccountChangeEvent accountChangeEvent = new AccountChangeEvent();
            accountChangeEvent.setAccountNo(accountPay.getAccountNo());
            accountChangeEvent.setAmount(accountPay.getPayAmount());
            accountChangeEvent.setTxNo(accountPay.getId());
            accountInfoService.updateAccountBalance(accountChangeEvent);
        }
        log.info("处理消息完成：{}", JSON.toJSONString(accountPay));
    }
}
