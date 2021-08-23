package fast.cloud.nacos.lb;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingService;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author qinfuxiang
 */
public class NacosDynamicServerListUpdater implements ServerListUpdater {
    private static Logger log = LoggerFactory.getLogger(NacosDynamicServerListUpdater.class);

    private CopyOnWriteArrayList<LBUpdater> lbUpdaters = new CopyOnWriteArrayList();

    private NamingService namingService;

    @Value("${ribbon.PollingServerListUpdater.initial-delay:1000}")
    private long initialDelay;
    @Value("${ribbon.PollingServerListUpdater.refresh-interval:30000}")
    private long refreshInterval;

    private NacosDiscoveryProperties properties;

    public NacosDynamicServerListUpdater(NamingService namingService, NacosDiscoveryProperties properties) {
        this.namingService = namingService;
        this.properties = properties;
    }

    @Override
    public void start(UpdateAction updateAction) {
        NacosDynamicServerListUpdater.LBUpdater lbUpdater = new NacosDynamicServerListUpdater.LBUpdater(updateAction);
        this.lbUpdaters.add(lbUpdater);
        lbUpdater.start();
        log.info("nacos dslu-started: {}, lbUpdater: {}, PollingServerListUpdater: initialDelay={}, refreshInterval={}",
                new Object[]{this, lbUpdater.getIdentity(), this.initialDelay, this.refreshInterval});
    }

    @Override
    public void stop() {
        log.info("nacos dslu-stopped: {}", this);
        Iterator iterator = this.lbUpdaters.iterator();

        while (iterator.hasNext()) {
            NacosDynamicServerListUpdater.LBUpdater lbUpdater = (NacosDynamicServerListUpdater.LBUpdater) iterator.next();

            try {
                lbUpdater.stop();
            } catch (Exception var4) {
                log.error("nacos dslu-stop-lbUpdater: " + lbUpdater.getIdentity(), var4);
            }
        }
    }

    @Override
    public String getLastUpdate() {
        return null;
    }

    @Override
    public long getDurationSinceLastUpdateMs() {
        return 0;
    }

    @Override
    public int getNumberMissedCycles() {
        return 0;
    }

    @Override
    public int getCoreThreads() {
        return 0;
    }

    class LBUpdater {
        Logger log = LoggerFactory.getLogger(NacosDynamicServerListUpdater.LBUpdater.class);
        private String serviceId;
        private volatile UpdateAction updateAction;
        private volatile BaseLoadBalancer lb;
        private NacosDynamicServerListWatcher nacosWatcher;
        private PollingServerListUpdater pollingServerListUpdater;
        private String identity;

        public LBUpdater(UpdateAction updateAction) {
            this.updateAction = updateAction;
            this.lb = this.getLoadBalancer(updateAction);
            this.serviceId = this.lb.getClientConfig().getClientName();
            this.pollingServerListUpdater = new PollingServerListUpdater(NacosDynamicServerListUpdater.this.initialDelay, NacosDynamicServerListUpdater.this.refreshInterval);
            this.nacosWatcher = new NacosDynamicServerListWatcher(NacosDynamicServerListUpdater.this.namingService, NacosDynamicServerListUpdater.this.properties, this);
        }

        public void start() {
            this.pollingServerListUpdater.start(this.updateAction);
            this.nacosWatcher.startWatch();
        }

        private BaseLoadBalancer getLoadBalancer(UpdateAction updateAction) {
            try {
                Class<?> bc = updateAction.getClass();
                Field field = bc.getDeclaredField("this$0");
                field.setAccessible(true);
                return (BaseLoadBalancer) field.get(updateAction);
            } catch (Exception var4) {
                this.log.error("nacos dslu-getlb", var4);
                throw new IllegalStateException("Not supported LB used", var4);
            }
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public UpdateAction getUpdateAction() {
            return this.updateAction;
        }

        public BaseLoadBalancer getLb() {
            return this.lb;
        }

        public PollingServerListUpdater getPollingServerListUpdater() {
            return this.pollingServerListUpdater;
        }

        public NacosDynamicServerListWatcher getNacosWatcher() {
            return nacosWatcher;
        }

        public String getIdentity() {
            if (this.identity == null) {
                this.identity = String.format("{serviceId: %s, lb: %s, updateAction: %s}", this.getServiceId(), this.getLb().hashCode(), this.getUpdateAction().hashCode());
            }

            return this.identity;
        }

        public void stop() {
            try {
                this.nacosWatcher.stop();
            } catch (Exception e) {
                this.log.error("nacos dslu-stop-watcher: " + this.getIdentity(), e);
            }

            this.pollingServerListUpdater.stop();
        }

        public void doUpdate() {
            this.getUpdateAction().doUpdate();
            this.log.info("nacos dslu-doUpdate: {}", this.getIdentity());
            this.serviceLog();

        }

        private void serviceLog() {
            List<Server> backwardList = this.getLb().getAllServers();
            StringBuilder serviceLog = new StringBuilder("");
            Iterator iterator = backwardList.iterator();

            while (iterator.hasNext()) {
                Server service = (Server) iterator.next();
                serviceLog.append(service.getHost());
                serviceLog.append(":");
                serviceLog.append(service.getPort());
                serviceLog.append(",");
            }

            this.log.info("[nacos dslu-LbServerList] [{}].{}", serviceLog, this.getIdentity());
        }
    }
}
