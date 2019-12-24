package fast.cloud.nacos.common.model.handler;

import fast.cloud.nacos.common.model.response.ApiResponse;
import fast.cloud.nacos.common.model.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice(annotations = {RestController.class})
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String returnTypeName = returnType.getParameterType().getName();
        return !"fast.cloud.nacos.common.model.response.ApiResponse".equals(returnTypeName) &&
                !"org.springframework.http.ResponseEntity".equals(returnTypeName);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        String name = methodParameter.getParameterType().getName();
        if ("void".equals(name)) {
            return new ApiResponse<>();
        }
        if (body instanceof String) {
            return JsonUtils.toString(new ApiResponse<>(body));
        }
        return new ApiResponse<>(body);
    }
}
