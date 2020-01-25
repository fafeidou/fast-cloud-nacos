package fast.cloud.nacos.tenant.dynamic.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Order(-1)
@Slf4j
@Component
public class DynamicDataSourceAspect {

    /**
     * 切换数据源
     * 获取类上面的bean，逻辑是如果类上面有，方法上面也有，默认用方法上面的进行覆盖
     *
     * @param point
     * @param dataSource
     */
    @Before("@within(dataSource)")
    public void switchDataSource(JoinPoint point, DataSource dataSource) {
        MethodSignature sign = (MethodSignature) point.getSignature();
        Method method = sign.getMethod();
        //获取方法上的注解
        DataSource targetAnnotation;
        targetAnnotation = method.getAnnotation(DataSource.class);
        if (targetAnnotation == null) {
            targetAnnotation = point.getTarget().getClass().getAnnotation(DataSource.class);
            if (targetAnnotation == null) {
                for (Class<?> cls : point.getClass().getInterfaces()) {
                    targetAnnotation = cls.getAnnotation(DataSource.class);
                    if (targetAnnotation != null) {
                        break;
                    }
                }
            }
        }

        if (targetAnnotation != null) {
            dataSource = targetAnnotation;
        }

        if (!DynamicDataSourceContextHolder.containDataSourceKey(dataSource.value())) {
            log.debug("DataSource [{}] doesn't exist, use default DataSource [{}] " + dataSource.value());
        } else {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(dataSource.value());
            log.debug("Switch DataSource to [" + DynamicDataSourceContextHolder.getDataSourceKey()
                    + "] in Method [" + point.getSignature() + "]");
        }
    }

    /**
     * 重置数据源
     *
     * @param point
     * @param dataSource
     */
    @After("@within(dataSource)")
    public void restoreDataSource(JoinPoint point, DataSource dataSource) {
        // 将数据源置为默认数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
        log.debug("Restore DataSource to [" + DynamicDataSourceContextHolder.getDataSourceKey()
                + "] in Method [" + point.getSignature() + "]");
    }
}
