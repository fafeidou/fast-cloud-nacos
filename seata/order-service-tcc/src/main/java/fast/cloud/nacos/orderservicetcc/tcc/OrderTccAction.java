package fast.cloud.nacos.orderservicetcc.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import java.math.BigDecimal;

@LocalTCC
public interface OrderTccAction {

    /*
    第一阶段的方法
    通过注解指定第二阶段的两个方法名

    BusinessActionContext 上下文对象，用来在两个阶段之间传递数据
    @BusinessActionContextParameter 注解的参数数据会被存入 BusinessActionContext
     */
    @TwoPhaseBusinessAction(name = "orderTccAction", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepareCreateOrder(BusinessActionContext businessActionContext,
        @BusinessActionContextParameter(paramName = "orderId") Integer orderId,
        @BusinessActionContextParameter(paramName = "userId") String userId,
        @BusinessActionContextParameter(paramName = "productId") String productId,
        @BusinessActionContextParameter(paramName = "count") Integer count,
        @BusinessActionContextParameter(paramName = "money") BigDecimal money);

    // 第二阶段 - 提交
    boolean commit(BusinessActionContext businessActionContext);

    // 第二阶段 - 回滚
    boolean rollback(BusinessActionContext businessActionContext);

}
