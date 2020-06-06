package fast.cloud.nacos.example.web.enums;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * type属性返回值的封装对象
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeResponse {

    @ApiModelProperty(value = "枚举code", name = "code", example = "1")
    private Integer code;

    @ApiModelProperty(value = "枚举value", name = "desc", example = "1")
    private String desc;

    @ApiModelProperty(value = "级联层级", name = "index", example = "1")
    private String index;
}
