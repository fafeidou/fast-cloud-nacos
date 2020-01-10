package fast.cloud.nacos.common.grpc.runner;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import fast.cloud.nacos.common.grpc.annoation.GRpcService;
import fast.cloud.nacos.common.grpc.config.GRpcServerProperties;
import fast.cloud.nacos.common.grpc.utils.NacosUtils;
import fast.cloud.nacos.common.grpc.utils.NetUtils;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class GRpcServerRunner implements CommandLineRunner, DisposableBean {
    @Autowired
    private AbstractApplicationContext applicationContext;

    @Autowired
    private GRpcServerProperties gRpcServerProperties;

    private Server server;

    private NamingService namingService = null;

    @Value("${spring.cloud.naocs.discovery.server-addr}")
    private String serverAddr;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting gRPC Server ...");

        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(gRpcServerProperties.getPort());

        // find and register all GRpcService-enabled beans
        for (BindableService bindableService : getTypedBeansWithAnnotation(GRpcService.class, BindableService.class)) {

            ServerServiceDefinition serviceDefinition = bindableService.bindService();
            serverBuilder.addService(serviceDefinition);
            log.info("'{}' service has been registered.", bindableService.getClass().getName());
        }
        //register nacos
        URI uri = URI.create("http://" + serverAddr);
        Properties properties = new Properties();
        properties.setProperty("serviceName", "demo");
        properties = NacosUtils.buildNacosProperties(uri, properties);
        this.namingService = NacosFactory.createNamingService(properties);
        registerNacos(properties);

        server = serverBuilder.build().start();
        log.info("gRPC Server started, listening on port {}.", gRpcServerProperties.getPort());
        startDaemonAwaitThread();

    }

    private void registerNacos(Properties properties) throws NacosException {
        namingService.registerInstance(properties.getProperty("serviceName", "test"), createInstance());
    }

    private Instance createInstance() {
        Instance instance = new Instance();
        instance.setIp(NetUtils.getLocalHost());
        instance.setPort(gRpcServerProperties.getPort());
        return instance;
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread() {
            @Override
            public void run() {
                try {
                    GRpcServerRunner.this.server.awaitTermination();
                } catch (InterruptedException e) {
                    log.error("gRPC server stopped.", e);
                }
            }

        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() throws Exception {
        log.info("Shutting down gRPC server ...");
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        log.info("gRPC server stopped.");
    }

    private <T> Collection<T> getTypedBeansWithAnnotation(Class<? extends Annotation> annotationType, Class<T> beanType) {
        return Stream.of(applicationContext.getBeanNamesForType(beanType))
                .filter(name -> {
                    BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(name);
                    if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                        StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                        return metadata.isAnnotated(annotationType.getName());
                    }
                    return null != applicationContext.getBeanFactory().findAnnotationOnBean(name, annotationType);
                })
                .map(name -> applicationContext.getBeanFactory().getBean(name, beanType))
                .collect(Collectors.toList());

    }
}
