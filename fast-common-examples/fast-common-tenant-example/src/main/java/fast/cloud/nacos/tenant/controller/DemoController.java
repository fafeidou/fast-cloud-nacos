package fast.cloud.nacos.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("test-tenant")
@RestController
public class DemoController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DemoMapper demoMapper;

    @RequestMapping("redis-save")
    public String save() {
        redisTemplate.opsForValue().set("hello", "world");
        return "ok";
    }

    @RequestMapping("mysql-query")
    public List<DemoEntity> mysqlQuery() {
        return demoMapper.selectList(new QueryWrapper<>());
    }
}
