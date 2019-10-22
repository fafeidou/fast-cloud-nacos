package fast.cloud.nacos.mybatis.condition;

import fast.cloud.nacos.common.model.condition.PageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DemoCondition extends PageCondition {
    private String name;
}
