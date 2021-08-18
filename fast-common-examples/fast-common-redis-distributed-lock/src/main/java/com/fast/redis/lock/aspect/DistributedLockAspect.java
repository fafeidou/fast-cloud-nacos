package com.fast.redis.lock.aspect;

import com.fast.redis.lock.DistributedLocker;
import com.fast.redis.lock.annotation.DistributedLock;
import com.fast.redis.util.SpelUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class DistributedLockAspect {

    @Resource
    DistributedLocker distributedLocker;

    @Pointcut("@annotation(com.fast.redis.lock.annotation.DistributedLock)")
    public void pointCut() {
    }

    /**
     * 环绕增强，尝试获取锁/释放锁
     *
     * @param joinPoint 切面
     * @return Object
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        DistributedLock annotation = AnnotationUtils.findAnnotation(targetMethod, DistributedLock.class);
        assert annotation != null;
        String key = getLockKey(targetMethod, joinPoint, annotation);
        boolean lockFlag = false;
        Object proceed = null;
        try {
            lockFlag = distributedLocker.tryLock(key, annotation.timeUnit(), annotation.waitTime(), annotation.leaseTime());

            if (lockFlag) {
                log.info("success to get distributed lock with key {}", key);
                proceed = joinPoint.proceed();
            }
        } catch (Exception exception) {
            log.error("exception occurred while getting distributed lock ", exception);
            return null;
        } finally {
            if (lockFlag) {
                distributedLocker.unlock(key);
                log.info("lock {} has been released", key);
            }
        }
        return proceed;
    }


    /**
     * 获取拦截到的方法,解析分布式锁key值（如果包含el表达式，则从中解析出内容）
     *
     * @param joinPoint 切点
     * @return redisKey
     */
    private String getLockKey(Method targetMethod,
                              ProceedingJoinPoint joinPoint, DistributedLock targetAnnotation) {
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < targetAnnotation.keys().length; i++) {
            String subKey = targetAnnotation.keys()[i];
//            StringUtil.contains(subKey, Constants.Symbol.SHARP)
            if (StringUtils.isNotBlank(subKey) && subKey.contains("#")) {
                stringBuilder.append(SpelUtil.parse(target, subKey, targetMethod, arguments));
            } else {
                stringBuilder.append(subKey);
            }
        }
        if (StringUtils.isNotBlank(targetAnnotation.prefix())) {
            return String.join(targetAnnotation.prefix(), targetAnnotation.separator(), stringBuilder);
        } else {
            return String.join(target.getClass().getName(), targetAnnotation.separator(), targetMethod.getName(), targetAnnotation.separator(), stringBuilder);
        }
    }
}
