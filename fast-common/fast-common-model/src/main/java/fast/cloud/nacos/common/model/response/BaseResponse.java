package fast.cloud.nacos.common.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseResponse<T> implements Serializable {
    private List<T> details;
}
