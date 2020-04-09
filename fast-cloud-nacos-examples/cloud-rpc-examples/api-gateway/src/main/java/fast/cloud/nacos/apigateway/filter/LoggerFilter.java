//package fast.cloud.nacos.apigateway.filter;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fast.cloud.nacos.apigateway.constants.FilterConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.RequestPath;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import javax.annotation.Resource;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class LoggerFilter implements GlobalFilter, Ordered {
//
//    @Resource
//    private ObjectMapper objectMapper;
//
//    private Map<String, Object> logger;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String id = request.getId();
//        RequestPath path = request.getPath();
//        MultiValueMap<String, String> queryParams = request.getQueryParams();
//        HttpHeaders requestHeaders = request.getHeaders();
//        String methodValue = request.getMethodValue();
//        URI uri = request.getURI();
//
//        /// request body 只能获取一次。在网关这一层，使用其他方案记录此信息。比如自定义全局过滤器，将 body 记录到 exchange 的一个自定义属性中。
//        // Flux<DataBuffer> body = request.getBody();
//
//        logger = new HashMap<>(1 << 4);
//        logger.put("logger", "request");
//        logger.put("id", id);
//        logger.put("path", path.value());
//        logger.put("queryParams", queryParams);
//        logger.put("requestHeaders", requestHeaders);
//        logger.put("method", methodValue);
//        logger.put("uri", uri.toString());
//        try {
//            log.info(objectMapper.writeValueAsString(logger));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        Object cachedRequestBodyObject = exchange.getAttributes().get(FilterConstant.CACHED_REQUEST_BODY_OBJECT_KEY);
//        if (cachedRequestBodyObject != null) {
//            byte[] body = (byte[]) cachedRequestBodyObject;
//            String string = new String(body);
//            log.info("request body:");
//            log.info(string);
//        }
//
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            ServerHttpResponse response = exchange.getResponse();
//
//            /// 暂不记录 cookies
//            // MultiValueMap<String, ResponseCookie> cookies = response.getCookies();
//
//            HttpStatus statusCode = response.getStatusCode();
//            HttpHeaders responseHeaders = response.getHeaders();
//
//            logger = new HashMap<>(1 << 4);
//            logger.put("logger", "response");
//            logger.put("responseHeaders", responseHeaders);
//            if (statusCode != null) {
//                logger.put("statusCode", statusCode.value());
//            }
//            try {
//                log.info(objectMapper.writeValueAsString(logger));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }));
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
//
