package fast.cloud.nacos.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.common.tenant.constants.DamCacheKeyEnum;
import fast.cloud.nacos.common.tenant.utils.RedisLock;
import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.entity.TenantDemoEntity;
import fast.cloud.nacos.tenant.mapper.DemoMapper;
import fast.cloud.nacos.tenant.mapper.TenantDemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fast.cloud.nacos.common.tenant.constants.DamCacheKeyEnum.SERVICE_NAME_METHOD_NAME_TIMES_PREFIX;

@RequestMapping("test-tenant")
@RestController
@Slf4j
public class DemoController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @GetMapping("redis-lock")
    public void redisLock1() {
        long timeoutInSeconds = TimeoutUtils.toSeconds(SERVICE_NAME_METHOD_NAME_TIMES_PREFIX.getTimeCount(),
                SERVICE_NAME_METHOD_NAME_TIMES_PREFIX.getTimeUnit());
        RedisLock redisLock = new RedisLock(stringRedisTemplate,
                SERVICE_NAME_METHOD_NAME_TIMES_PREFIX.assembleKey(1l),  (int) timeoutInSeconds, 100);
        try {
            if (!redisLock.tryLock()) {
                Thread.sleep(10000);
                log.warn("获取防止asset重复入库redis锁失败！");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            redisLock.unlock();
        }
    }

}
