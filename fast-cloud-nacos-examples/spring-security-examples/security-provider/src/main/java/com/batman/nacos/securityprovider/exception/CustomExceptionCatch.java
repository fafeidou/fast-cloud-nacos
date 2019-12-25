package com.batman.nacos.securityprovider.exception;

import fast.cloud.nacos.common.model.handler.GlobalExceptionHandler;
import fast.cloud.nacos.common.model.model.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomExceptionCatch extends GlobalExceptionHandler {
    static {
        //除了CustomException以外的异常类型及对应的错误代码在这里定义,，如果不定义则统一返回固定的错误信息
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
