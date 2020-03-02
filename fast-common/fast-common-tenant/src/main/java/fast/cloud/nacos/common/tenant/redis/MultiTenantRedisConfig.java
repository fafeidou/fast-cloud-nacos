package fast.cloud.nacos.common.tenant.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass({JedisConnectionFactory.class, StringRedisTemplate.class, RedisProperties.class})
public class MultiTenantRedisConfig {

    @Autowired
    private TenantStringSerializer tenantStringSerializer;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory getRedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    @Primary
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(getRedisConnectionFactory());

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(tenantStringSerializer);
        redisTemplate.setHashKeySerializer(tenantStringSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public TenantStringSerializer getTenantStringSerializer() {
        return new TenantStringSerializer();
    }

}
