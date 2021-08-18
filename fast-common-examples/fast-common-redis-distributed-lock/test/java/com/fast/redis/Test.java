package com.fast.redis;

import com.fast.redis.model.LockModel;
import com.fast.redis.service.RedisLockService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private RedisLockService redisLockService;

    @org.junit.Test
    public void test() {
        LockModel lockModel = new LockModel();
        lockModel.setId("333");
        redisLockService.testLock(lockModel);
    }


}
