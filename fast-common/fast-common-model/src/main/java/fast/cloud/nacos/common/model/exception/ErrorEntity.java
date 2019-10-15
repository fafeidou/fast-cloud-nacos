package fast.cloud.nacos.common.model.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Map;

/**
 * 错误内容定义
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ErrorEntity {

    /**
     * 字段名称
     */
    private String field;

    /**
     * 错误文言
     */
    private String message;

    /**
     * 错误消息参数
     */
    private Map<String, Object> params;

    public ErrorEntity(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ErrorEntity() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
