package fast.cloud.nacos.example.web.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@ApiModel
public class EnumTypeRequest {

    @ApiModelProperty(name = "枚举名称", required = true)
    private List<String> enumNames;
}
