package com.fast.redis.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {

    /**
     * 以默认配置获取锁，等待锁默认5s，锁租期默认3s
     *
     * @param lockKey lockKey
     * @return RLock
     */
    RLock lock(String lockKey) ;

    /**
     * 自定义超时时间的锁，最多等待timeout秒，
     *
     * @param lockKey lockKey
     * @param waitTime 超时时间，单位为秒
     * @return lock
     */
    RLock lock(String lockKey, long waitTime);

    /**
     * 自定义超时时间和时间单位的锁，
     *
     * @param lockKey lockKey
     * @param waitTime 超时时间
     * @param unit 时间单位
     * @return lock
     */
    RLock lock(String lockKey, TimeUnit unit, long waitTime);

    /**
     * 尝试加锁。自定义时间单位和锁等待时间，租期默认3s
     *
     * @param lockKey lockKey
     * @param unit 时间单位
     * @param waitTime 超时时间
     * @return boolean
     */
    boolean tryLock(String lockKey, TimeUnit unit, long waitTime);


    /**
     *  尝试加锁，自定义时间单位和锁等待时间以及锁过期时间
     *
     * @param lockKey key
     * @param timeUnit 时间单位
     * @param waitTime 等待超时时间
     * @param leaseTime 锁租期，超过leaseTime自动释放
     * @return boolean
     */
    boolean tryLock(String lockKey,TimeUnit timeUnit, long waitTime , long leaseTime);

    /**
     * 公平锁,多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     * 尝试加锁，最多等待waitTime后强制获取锁，上锁以后leaseTime自动解锁
     *
     * @param lockKey   锁key
     * @param unit      锁时间单位
     * @param waitTime  等到最大时间，强制获取锁
     * @param leaseTime 锁自动时间，
     * @return 如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false
     */
    boolean fairLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);


    /**
     *  获取公平锁，单位为秒
     *
     * @param lockKey lockKey
     * @param waitTime 等待时间
     * @param leaseTime 租期
     * @return boolean
     */
    boolean fairLock(String lockKey, long waitTime,long leaseTime);

    /**
     * 解锁
     *
     * @param lockKey lockKey
     */

    void unlock(String lockKey);

    /**
     * 解锁RLock
     *
     * @param lock
     */
    void unlock(RLock lock);

}
