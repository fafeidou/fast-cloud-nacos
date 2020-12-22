package fast.cloud.nacos.rocketmq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPay implements Serializable {


    /**
     *  事务号
     */
    private String id;

    /**
     * 账号
     */
    private String accountNo;
    /**
     * 变动金额
     */
    private double payAmount;
    /**
     * 充值结果
     */
    private String result;

}
