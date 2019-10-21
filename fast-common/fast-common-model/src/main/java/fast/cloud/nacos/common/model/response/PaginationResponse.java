package fast.cloud.nacos.common.model.response;

import lombok.Data;

@Data
public class PaginationResponse<T> extends BaseResponse<T> {
    private Long total;
}
