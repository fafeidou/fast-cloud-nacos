package fast.cloud.nacos.common.tenant.constants;

import java.util.concurrent.TimeUnit;

/**
 * 缓存有效期配置
 */
public interface ICacheKeyTimeConfig {

    Long getTimeCount();

    TimeUnit getTimeUnit();

}
