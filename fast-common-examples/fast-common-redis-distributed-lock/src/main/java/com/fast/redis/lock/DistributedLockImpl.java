package com.fast.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DistributedLockImpl implements DistributedLocker{
    /**
     * 默认时间单位：秒
     */
    public static final TimeUnit DEFAULT_TIME_UNIT= TimeUnit.SECONDS;

    /**
     * 默认锁等待超时时间
     */
    public static final int DEFAULT_TIMEOUT=8;
    /**
     * 默认锁过期时间
     */
    public static final int DEFAULT_LEASE_TIME=3;


    @Resource
    private RedissonClient redissonClient;


    @Override
    public RLock lock(String lockKey) {
        RLock lock =redissonClient.getLock(lockKey);
        try {
            lock.tryLock(DEFAULT_TIMEOUT,DEFAULT_LEASE_TIME,DEFAULT_TIME_UNIT);
        } catch (InterruptedException e) {
            log.error("get lock with key {} failed,cause ",lockKey,e);
            return null;
        }
        return lock;
    }

    @Override
    public RLock lock(String lockKey, long timeout) {
        return lock(lockKey,DEFAULT_TIME_UNIT,timeout);
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock =redissonClient.getLock(lockKey);
        try {
            lock.tryLock(timeout,DEFAULT_LEASE_TIME,unit);
        } catch (InterruptedException e) {
            log.error("get lock with key {} failed. cause",lockKey,e);
            return null;
        }
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, long timeout) {
        return  tryLock(lockKey,unit,timeout,DEFAULT_LEASE_TIME);
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit timeUnit, long waitTime, long leaseTime) {
        RLock lock=redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime,leaseTime,timeUnit);
        } catch (InterruptedException e) {
            log.error("get lock with key {} failed. cause",lockKey,e);
            return false;
        }
    }

    @Override
    public boolean fairLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock=redissonClient.getFairLock(lockKey);
        try {
            return  lock.tryLock(waitTime,leaseTime,unit);
        } catch (InterruptedException e) {
            log.error("get lock with key {} failed. cause",lockKey,e);
            return false;
        }
    }

    @Override
    public boolean fairLock(String lockKey, long waitTime, long leaseTime) {
        return fairLock(lockKey,DEFAULT_TIME_UNIT,waitTime,leaseTime);
    }


    @Override
    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Throwable e) {
            String msg = String.format("UNLOCK FAILED: key=%s", lockKey);
            throw new IllegalStateException(msg, e);
        }
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }
}
