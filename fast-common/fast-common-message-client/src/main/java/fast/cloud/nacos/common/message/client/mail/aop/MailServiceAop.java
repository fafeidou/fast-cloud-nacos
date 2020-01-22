package fast.cloud.nacos.common.message.client.mail.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import fast.cloud.nacos.common.message.client.mail.model.TenantMailModel;
import fast.cloud.nacos.common.tenant.context.TenantStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Aspect
@Service
public class MailServiceAop {

    @Pointcut(value = "execution(public * fast.cloud.nacos.common.message.client.mail.service..*.*(..))")
    public void methodPointcut() {
    }

    @Around(value = "methodPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        TenantMailModel tenantMailModel = null;
        List<TenantMailModel> tenantMailModelList = null;

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof TenantMailModel) {
                tenantMailModel = (TenantMailModel) arg;
            } else if (arg instanceof List) {
                if (((List) arg).get(0) instanceof TenantMailModel) {
                    tenantMailModelList = (List<TenantMailModel>) arg;
                }
            }
        }

        String tenantId = TenantStore.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            if (Objects.nonNull(tenantMailModel)) {
                tenantMailModel.setTenantId(tenantId);
            } else {
                Optional.ofNullable(tenantMailModelList).ifPresent(list -> list.forEach(val -> val.setTenantId(tenantId)));
            }
        } else {
            String messageKey;
            if (Objects.nonNull(tenantMailModel)) {
                messageKey = tenantMailModel.getMsgKey();
            } else {
                messageKey = tenantMailModelList.stream().map(TenantMailModel::getMsgKey)
                        .filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
            }
            log.error("message keys {} can not get tenantId ,use default mail provider", messageKey);
        }
        //增加msgKey,用于追踪问题
        String msgKey = UUID.randomUUID().toString();
        if (Objects.nonNull(tenantMailModel)) {
            if (StringUtils.isBlank(tenantMailModel.getMsgKey())) {
                tenantMailModel.setMsgKey(msgKey);
                log.info("send mail msg key : {}", msgKey);
            } else {
                log.info("send mail msg key : {}", tenantMailModel.getMsgKey());
            }
        } else {
            if (CollectionUtils.isNotEmpty(tenantMailModelList)) {
                if (tenantMailModelList.stream().allMatch(val -> StringUtils.isBlank(val.getMsgKey()))) {
                    tenantMailModelList.forEach(val -> val.setMsgKey(msgKey));
                    log.info("send mail msg key : {}", msgKey);
                } else {
                    log.info("send mail msg key : {}", tenantMailModelList.stream().filter(val -> StringUtils.isNotBlank(val.getMsgKey())).map(TenantMailModel::getMsgKey).findFirst().orElse(""));
                }
            }
        }

        Object proceed = joinPoint.proceed();

        return proceed;
    }

}
