package fast.cloud.nacos.orderservicetcc.controller;

import fast.cloud.nacos.orderservicetcc.service.OrderService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname OrderController
 * @Description TODO
 * @Date 2020/4/4 21:04
 * @Created by qinfuxiang
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Resource
    private OrderService orderService;


    /**
     * 下单：插入订单表、扣减库存，模拟回滚
     */
    @RequestMapping("/placeOrder/commit")
    public Boolean placeOrderCommit() {
        orderService.placeOrder("1", "product-1", 1);
        return true;
    }

    /**
     * 下单：插入订单表、扣减库存，模拟回滚
     */
    @RequestMapping("/placeOrder/rollback")
    public Boolean placeOrderRollback() {
        // product-2 扣库存时模拟了一个业务异常,
        orderService.placeOrder("1", "product-2", 1);
        return true;
    }


    @RequestMapping("/placeOrder")
    public Boolean placeOrder(String userId, String commodityCode, Integer count) {
        orderService.placeOrder(userId, commodityCode, count);
        return true;
    }
}