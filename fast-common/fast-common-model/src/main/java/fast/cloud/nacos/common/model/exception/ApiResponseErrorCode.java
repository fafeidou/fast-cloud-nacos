package fast.cloud.nacos.common.model.exception;

/**
 * ApiResponseCode定义
 *
 * @author donghk
 */
public enum ApiResponseErrorCode {

    // @formatter:off
    CODE_0(0, "成功"),
    CODE_101(101, "参数错误"),
    CODE_102(102, "文件错误"),
    CODE_999(999, "未知异常");
    // @formatter:on

    /**
     * 编号
     */
    private Integer code;

    /**
     * 编号信息
     */
    private String message;

    /**
     * 构造器
     *
     * @param code
     * @param message
     */
    ApiResponseErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过编号获取枚举对象
     *
     * @param message
     */
    public static ApiResponseErrorCode getByMessage(String message) {
        for (ApiResponseErrorCode item : ApiResponseErrorCode.values()) {
            if (item.getMessage().equals(message)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 通过编号信息获取枚举对象
     *
     * @param code
     */
    public static ApiResponseErrorCode getByCode(String code) {
        for (ApiResponseErrorCode item : ApiResponseErrorCode.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
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

}
