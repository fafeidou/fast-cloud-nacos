package fast.cloud.nacos.orderservicetcc.service;

import fast.cloud.nacos.orderservicetcc.feign.StorageFeignClient;
import fast.cloud.nacos.orderservicetcc.tcc.OrderTccAction;
import fast.cloud.nacos.orderservicetcc.utils.UUIDUtils;
import io.seata.spring.annotation.GlobalTransactional;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2020/4/4 21:03
 * @Created by qinfuxiang
 */
@Service
public class OrderService {

    @Resource
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderTccAction orderTccAction;


    /**
     * 下单：创建订单、减库存，涉及到两个服务
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(String userId, String commodityCode, Integer count) {
        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));

        // 这里修改成调用 TCC 第一节端方法
        orderTccAction.prepareCreateOrder(
            null,
            UUIDUtils.getUUIDInOrderId(),
            userId,
            commodityCode,
            count,
            orderMoney);

        // 修改库存
        storageFeignClient.deduct(commodityCode, count);
    }

}
