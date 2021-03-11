package fast.cloud.nacos.orderservicetcc.tcc;

import fast.cloud.nacos.orderservicetcc.model.Order;
import fast.cloud.nacos.orderservicetcc.repository.OrderDAO;
import io.seata.rm.tcc.api.BusinessActionContext;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class OrderTccActionImpl implements OrderTccAction {

    @Autowired
    private OrderDAO orderMapper;

    @Transactional
    @Override
    public boolean prepareCreateOrder(BusinessActionContext businessActionContext, Integer orderId, String userId,
        String productId, Integer count, BigDecimal money) {
        log.info("创建 order 第一阶段，预留资源 - " + businessActionContext.getXid());

        Order order = new Order(orderId, userId, productId, count, money, 0);
        orderMapper.insert(order);

        //事务成功，保存一个标识，供第二阶段进行判断
        ResultHolder.setResult(getClass(), businessActionContext.getXid(), "p");
        return true;
    }

    @Transactional
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        log.info("创建 order 第二阶段提交，修改订单状态1 - " + businessActionContext.getXid());

        // 防止幂等性，如果commit阶段重复执行则直接返回
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }

        //        Long orderId = (Long) businessActionContext.getActionContext("orderId");
        long orderId = Long.parseLong(businessActionContext.getActionContext("orderId").toString());
        orderMapper.updateStatus(orderId, 1);

        //提交成功是删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }

    @Transactional
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        log.info("创建 order 第二阶段回滚，删除订单 - " + businessActionContext.getXid());

        //第一阶段没有完成的情况下，不必执行回滚
        //因为第一阶段有本地事务，事务失败时已经进行了回滚。
        //如果这里第一阶段成功，而其他全局事务参与者失败，这里会执行回滚
        //幂等性控制：如果重复执行回滚则直接返回
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }

        //Long orderId = (Long) businessActionContext.getActionContext("orderId");
        long orderId = Long.parseLong(businessActionContext.getActionContext("orderId").toString());
        orderMapper.deleteById(orderId);

        //回滚结束时，删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }
}
