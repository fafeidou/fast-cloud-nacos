package fast.cloud.nacos.lb;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class NacosDynamicServerListWatcher {
    private static Logger log = LoggerFactory.getLogger(NacosDynamicServerListWatcher.class);

    private NamingService namingService;

    private final NacosDiscoveryProperties properties;
    private NacosDynamicServerListUpdater.LBUpdater lbUpdater;

    public NacosDynamicServerListWatcher(NamingService namingService, NacosDiscoveryProperties properties, NacosDynamicServerListUpdater.LBUpdater lbUpdater) {
        this.namingService = namingService;
        this.properties = properties;
        this.lbUpdater = lbUpdater;
    }

    public void startWatch() {

        log.info("nacos dslw-start: {}, serviceName: {}", this.lbUpdater.getIdentity(), lbUpdater.getServiceId());

        try {
            namingService.subscribe(lbUpdater.getServiceId(), Arrays.asList(properties.getClusterName()), event -> {
                if (event instanceof NamingEvent) {
                    NamingEvent namingEvent = (NamingEvent) event;
                    System.out.println("服务名：" + namingEvent.getServiceName());
                    System.out.println("实例：" + namingEvent.getInstances());
                    lbUpdater.doUpdate();
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public void stop() {

    }


}
