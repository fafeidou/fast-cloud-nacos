package com.fast.redis.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 分布式锁的key值,非空。支持el表达式获取入参对象参数
     */
    String[] keys();

    /**
     * 分布式锁key前缀，默认采用类全限定名:方法名
     */
    String prefix() default "";

    /**
     * key与前缀的分隔符
     */
    String separator() default ":";

    /**
     * 等待获取锁的时间
     */
    long waitTime() default 8;

    /**
     * 锁的租期，超时后自动释放
     */
    long leaseTime() default 3;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}