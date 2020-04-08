package fast.cloud.nacos.websocket.constants;

import fast.cloud.nacos.common.model.model.ResultCode;

/**
 * @Classname ConstantEnum
 * @Description TODO
 * @Date 2020/4/8 14:54
 * @Created by qinfuxiang
 */
public enum ConstantEnum implements ResultCode {
    WEB_SOCKET_PARAM_ERROR(false,20000,"")
    ;

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private ConstantEnum(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
