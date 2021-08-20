package fast.cloud.nacos;


import fast.cloud.nacos.warmup.WarmUpComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

public class AutoRegistration implements ApplicationListener<ApplicationReadyEvent>, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AutoRegistration.class);
    private List<WarmUpComponent> warmUpComponents;
    private DelayedNacosAutoRegistration delayedNacosAutoRegistration;

    public AutoRegistration(List<WarmUpComponent> warmUpComponents, DelayedNacosAutoRegistration delayedNacosAutoRegistration) {
        this.warmUpComponents = warmUpComponents;
        this.delayedNacosAutoRegistration = delayedNacosAutoRegistration;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("AutoRegistration started.");

        try {
            this.warmUp();
            this.doRegistration();
        } catch (Throwable var7) {
            log.error("AutoRegistration failed.", var7);
        } finally {
            log.info("AutoRegistration finished.");
        }

    }

    @Override
    public int getOrder() {
        return 2147483647;
    }

    private void warmUp() throws Throwable {
        log.info("WarmUp started.");

        try {
            Iterator iterator = this.warmUpComponents.iterator();

            while (iterator.hasNext()) {
                WarmUpComponent warmUpComponent = (WarmUpComponent) iterator.next();
                this.warmUp(warmUpComponent);
            }
        } catch (Throwable var7) {
            log.error("WarmUp error", var7);
            throw var7;
        } finally {
            log.info("WarmUp finished.");
        }
    }

    private void warmUp(WarmUpComponent warmUpComponent) throws Throwable {
        log.info("WarmUp component {} started.", warmUpComponent.name());

        try {
            warmUpComponent.warmUp();
        } catch (Throwable var7) {
            if (!warmUpComponent.ignoreWarmUpFail()) {
                throw var7;
            }

            log.error("WarmpUp component fail: {}, ignore it.", warmUpComponent.name(), var7);
        } finally {
            log.info("WarmUp component {} finished.", warmUpComponent.name());
        }

    }

    private void doRegistration() {
        log.info("DoRegistration started");

        try {
            this.delayedNacosAutoRegistration.doStart();
        } catch (Throwable var6) {
            log.error("DoRegistration failed", var6);
            throw var6;
        } finally {
            log.info("DoRegistration finished");
        }

    }
}
