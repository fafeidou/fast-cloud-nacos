package fast.cloud.nacos.securityapigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //从头取出jwt令牌
    public String getJwtFromHeader(ServerHttpRequest request) {
        //取出头信息
        HttpHeaders headers = request.getHeaders();
        List<String> authorizations = headers.get("Authorization");

        if (CollectionUtils.isEmpty(authorizations)) {
            return null;
        }
        if (!authorizations.get(0).startsWith("Bearer ")) {
            return null;
        }
        //取到jwt令牌
        String jwt = authorizations.get(0).substring(7);
        return jwt;


    }

    //从cookie取出token
    //查询身份令牌
    public String getTokenFromCookie(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (!Objects.isNull(cookies)) {
            HttpCookie httpCookie = cookies.getFirst("uid");
            if (!Objects.isNull(httpCookie)) {
                return httpCookie.getValue();
            }
        }
        return null;
    }

    //查询令牌的有效期
    public long getExpire(String access_token) {
        //key
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire;
    }
}
