package fast.cloud.nacos.common.model.exception;

import fast.cloud.nacos.common.model.model.ResultCode;

public class ExceptionCast {

    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
