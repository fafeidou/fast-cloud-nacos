package fast.cloud.nacos.securityapigateway.filter;

import fast.cloud.nacos.common.model.exception.ExceptionCast;
import fast.cloud.nacos.common.model.model.CommonCode;
import fast.cloud.nacos.securityapigateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenGlobalFilter implements GlobalFilter {
    @Autowired
    AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String tokenFromCookie = authService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(tokenFromCookie)) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        //从header中取jwt
        String jwtFromHeader = authService.getJwtFromHeader(request);
        if (StringUtils.isEmpty(jwtFromHeader)) {
            //拒绝访问
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        //从redis取出jwt的过期时间
        long expire = authService.getExpire(tokenFromCookie);
        if (expire < 0) {
            //拒绝访问
            ExceptionCast.cast(CommonCode.FORBIDDEN);
        }
        return chain.filter(exchange);
    }
}
