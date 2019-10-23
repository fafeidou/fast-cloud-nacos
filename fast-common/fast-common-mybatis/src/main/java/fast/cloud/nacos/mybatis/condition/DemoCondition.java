package fast.cloud.nacos.mybatis.condition;

import fast.cloud.nacos.common.model.condition.PageCondition;
import fast.cloud.nacos.mybatis.bean.request.MatchType;
import fast.cloud.nacos.mybatis.bean.request.QueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DemoCondition extends PageCondition {
    @QueryCondition(func = MatchType.like)
    private String name;
}
