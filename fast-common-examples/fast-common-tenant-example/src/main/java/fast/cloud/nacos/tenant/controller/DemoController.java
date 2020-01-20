package fast.cloud.nacos.tenant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test-redis-tenant")
@RestController
public class DemoController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("save")
    public String save() {
        redisTemplate.opsForValue().set("hello", "world");
        return "ok";
    }

}
