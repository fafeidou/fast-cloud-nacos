package fast.cloud.nacos.common.model.condition;

import fast.cloud.nacos.common.model.request.CommonSearchRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseCommonPaginationSearchRequest<T extends SearchCondition> extends CommonSearchRequest<T> {
    private PageCondition pagination;
}
