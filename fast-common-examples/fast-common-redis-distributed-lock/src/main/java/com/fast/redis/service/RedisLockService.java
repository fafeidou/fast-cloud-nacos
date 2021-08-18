package com.fast.redis.service;

import com.fast.redis.lock.annotation.DistributedLock;
import com.fast.redis.model.LockModel;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author qinfuxiang
 */
@Service
public class RedisLockService {

    @DistributedLock(prefix = "redis-lock:", keys = "#lockModel.id",waitTime = 0,leaseTime = 8)
    public void testLock(LockModel lockModel) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
