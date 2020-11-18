package fast.cloud.nacos.prometheus.example.aop;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import javax.annotation.PostConstruct;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qinfuxiang
 * @since 2020/11/18 12:46
 */
@Component
@Aspect
public class APICounterAop {

    @Pointcut(value = "@annotation(fast.cloud.nacos.prometheus.example.aop.ApiCount)")
    public void pointCut() {

    }

    @Autowired
    MeterRegistry registry;
    private Counter counter;

    @PostConstruct
    private void init() {
        counter = registry.counter("requests_total", "status", "success");
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.out.println("do before");
        counter.increment(); //请求计数
    }

    @AfterReturning(returning = "returnVal", pointcut = "pointCut()")
    public void doAfterReturning(Object returnVal) {
        System.out.println("do after");
    }
}
