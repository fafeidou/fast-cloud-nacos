package fast.cloud.nacos.common.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import fast.cloud.nacos.common.model.exception.ApiResponseErrorCode;
import fast.cloud.nacos.common.model.exception.ErrorEntity;
import fast.cloud.nacos.common.model.model.ResultCode;
import fast.cloud.nacos.common.model.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Restful API 返回JSON结果: { "code":0, "message":"成功","errors": [ErrorEntity],
 * "data":T }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Slf4j
public class ApiResponse<T> {

    /**
     * 系统状态
     */
    @JsonProperty("code")
    private Integer code;

    /**
     * 错误消息
     */
    @JsonProperty("message")
    private String message;

    /**
     * 错误内容列表
     */
    @JsonProperty("errors")
    private List<ErrorEntity> errors;

    /**
     * 返回结果内容
     */
    @JsonProperty("data")
    private T data;

    public ApiResponse(ResultCode resultCode) {
        this(resultCode.code(), resultCode.message(), null, null);
    }

    public ApiResponse(ResultCode resultCode, T data) {
        this(resultCode.code(), ApiResponseErrorCode.CODE_0.getMessage(), data, null);
    }

    /**
     * 成功，没数据
     */
    public ApiResponse() {
        this(ApiResponseErrorCode.CODE_0.getCode(),
                ApiResponseErrorCode.CODE_0.getMessage(), null, null);
    }

    /**
     * 成功，有数据
     */
    public ApiResponse(T data) {
        this(ApiResponseErrorCode.CODE_0.getCode(),
                ApiResponseErrorCode.CODE_0.getMessage(), data, null);
    }

    /**
     * 指定code/message
     */
    public ApiResponse(ApiResponseErrorCode apiResponseErrorCode) {
        this(apiResponseErrorCode.getCode(), apiResponseErrorCode.getMessage(),
                null, null);
    }

    /**
     * 指定code + data
     */
    public ApiResponse(ApiResponseErrorCode apiResponseErrorCode, T data) {
        this(apiResponseErrorCode.getCode(), apiResponseErrorCode.getMessage(),
                data, null);
    }

    /**
     * 指定code + errors
     */
    public ApiResponse(ApiResponseErrorCode apiResponseErrorCode,
                       List<ErrorEntity> errors) {
        this(apiResponseErrorCode.getCode(), apiResponseErrorCode.getMessage(),
                null, errors);
    }

    protected ApiResponse(Integer code, String message, T data,
                          List<ErrorEntity> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorEntity> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

}
