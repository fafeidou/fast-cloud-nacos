package fast.cloud.nacos.common.tenant.constants;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 缓存键及前缀枚举类
 */
@Getter
public enum DamCacheKeyEnum implements ICacheKeyTimeConfig {
    SERVICE_NAME_METHOD_NAME_TIMES_PREFIX(
            "serviceName.methodName.times.%s", "测试", 1L, TimeUnit.HOURS),
    ;

    private String keyOrPrefix;

    private String description;

    private Long timeCount;

    private TimeUnit timeUnit;

    DamCacheKeyEnum(String keyOrPrefix, String description, Long timeCount, TimeUnit timeUnit) {
        this.keyOrPrefix = keyOrPrefix;
        this.description = description;
        this.timeCount = timeCount;
        this.timeUnit = timeUnit;
    }

    public String assembleKey(Long id) {
        return String.format(this.keyOrPrefix, id);
    }

}
