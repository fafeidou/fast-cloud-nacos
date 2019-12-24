package fast.cloud.nacos.common.model.handler;

import fast.cloud.nacos.common.model.exception.*;
import fast.cloud.nacos.common.model.model.ResultCode;
import fast.cloud.nacos.common.model.response.ApiResponse;
import fast.cloud.nacos.common.model.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
@SuppressWarnings({"unchecked"})
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler */ {

    private static final Logger logger = LoggerFactory
            .getLogger(GlobalExceptionHandler.class);

    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ApiResponse customException(CustomException customException) {
        //记录日志
        logger.error("catch exception:{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ApiResponse(resultCode);
    }

    /**
     * 请求参数业务验证错误
     */
    @ExceptionHandler(RequestParamNotValidException.class)
    public ResponseEntity handleRequestParamNotValidException(
            RequestParamNotValidException ex) {

        logger.warn("参数业务验证错误： {}", ex.getBindingResult().getAllErrors());

        try {
            // 错误对象
            ApiResponse domain = new ApiResponse();
            List<ErrorEntity> errorEntitys = new ArrayList<>();

            // 获取错误信息
            BindingResult errors = ex.getBindingResult();

            errors.getFieldErrors().forEach(error -> {
                ErrorEntity apiError = new ErrorEntity();
                apiError.setMessage(error.getCode());
                apiError.setField(error.getField());
                if (Objects.nonNull(error.getArguments())
                        && Objects.nonNull(error.getArguments()[0])) {
                    if (error.getArguments()[0] instanceof Map) {
                        apiError.setParams((Map) error.getArguments()[0]);
                    }
                }

                errorEntitys.add(apiError);
            });

            domain.setErrors(errorEntitys);
            domain.setCode(ApiResponseErrorCode.CODE_101.getCode());
            domain.setMessage(ApiResponseErrorCode.CODE_101.getMessage());

            return new ResponseEntity(domain, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {

            logger.error("GlobalHandle构建response发生错误：", ex);

            // 错误对象
            ApiResponse domain = new ApiResponse(ApiResponseErrorCode.CODE_999);

            return new ResponseEntity(domain, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 表单属性外的验证不通过
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> badRequestException(
            BadRequestException bre) {

        logger.warn("请求错误： {}", bre.getMessage());

        // 错误对象
        ApiResponse domain = new ApiResponse();

        // 有详细错误抛出
        if (Objects.nonNull(bre.getMessage())) {
            List<ErrorEntity> errorEntitys = new ArrayList<>();
            ErrorEntity apiError = new ErrorEntity();
            apiError.setMessage(bre.getMessage());
            apiError.setParams(bre.getParams());
            errorEntitys.add(apiError);
            domain.setErrors(errorEntitys);
        }

        domain.setCode(ApiResponseErrorCode.CODE_101.getCode());
        domain.setMessage(ApiResponseErrorCode.CODE_101.getMessage());

        return new ResponseEntity(domain, HttpStatus.BAD_REQUEST);

    }

    /**
     * 文件上传异常处理
     */
    @ExceptionHandler(FileRequestException.class)
    @ResponseBody
    public String fileRequestException(FileRequestException fre) {

        logger.error("文件上传异常：{}", fre.getMessage());

        // 错误对象
        ApiResponse domain = new ApiResponse();

        domain.setCode(ApiResponseErrorCode.CODE_102.getCode());
        domain.setMessage(fre.getMessage());

        return JsonUtils.toString(domain);

    }

    /**
     * 未知异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity unknownException(Exception e) {

        logger.error("未知异常", e);

        return new ResponseEntity(e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiResponse resolveConstraintViolationException(ConstraintViolationException ex) {
        ApiResponse apiResponse = new ApiResponse(ApiResponseErrorCode.CODE_101);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                msgBuilder.append(constraintViolation.getMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
            apiResponse.setMessage(errorMessage);
            return apiResponse;
        }
        apiResponse.setMessage(ex.getMessage());
        return apiResponse;
    }

    /**
     * 用来处理bean validation异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponse apiResponse = new ApiResponse(ApiResponseErrorCode.CODE_101);
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
            apiResponse.setMessage(errorMessage);
            return apiResponse;
        }
        apiResponse.setMessage(ex.getMessage());
        return apiResponse;
    }

}
