package fast.cloud.nacos.common.model.condition;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageCondition implements Serializable {
    private Integer page;
    private Integer size;
}
