package fast.cloud.nacos.common.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaginationResponse<T> extends BaseResponse<T> {
    private Long total;
}
