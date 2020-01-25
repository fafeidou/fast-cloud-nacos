package fast.cloud.nacos.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.entity.TenantDemoEntity;
import fast.cloud.nacos.tenant.mapper.DemoMapper;
import fast.cloud.nacos.tenant.mapper.TenantDemoMapper;
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

    @Autowired
    private TenantDemoMapper tenantDemoMapper;

    @RequestMapping("redis-save")
    public String save() {
        redisTemplate.opsForValue().set("hello", "world");
        return "ok";
    }

    @RequestMapping("mysql-query")
    public List<DemoEntity> mysqlQuery() {
        return demoMapper.selectList(new QueryWrapper<>());
    }

    @RequestMapping("mysql-tenant")
    public List<TenantDemoEntity> mysqlQueryTenant() {
        Page<TenantDemoEntity> page = new Page<>();
        return tenantDemoMapper.selectTenantDemoPage(page);
    }
}
