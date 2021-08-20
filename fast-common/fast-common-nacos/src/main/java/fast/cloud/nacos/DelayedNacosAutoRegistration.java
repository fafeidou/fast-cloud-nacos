package fast.cloud.nacos;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

public class DelayedNacosAutoRegistration extends NacosAutoServiceRegistration {
    private static final Logger log = LoggerFactory.getLogger(DelayedNacosAutoRegistration.class);
    @Value("${server.port}")
    private int serverPort;

    public DelayedNacosAutoRegistration(ServiceRegistry<Registration> serviceRegistry,
                                        AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                        NacosRegistration registration) {
        super(serviceRegistry, autoServiceRegistrationProperties, registration);
    }

    @Override
    public void start() {
        log.info("Use {}. Will start registration later. Server.port: {}", this.getClass().getSimpleName(), this.getPort().get());
    }

    public void doStart() {
        int currentPort = this.getPort().get();
        if (currentPort == 0) {
            log.info("doStart {}, server.port: {}, configured: {}", new Object[]{this.getClass().getSimpleName(), currentPort, this.serverPort});
            this.getPort().set(this.serverPort);
        }

        super.start();
    }

}