package fast.cloud.nacos.grpc.starter;

import com.alibaba.nacos.api.naming.NamingService;
import fast.cloud.nacos.grpc.starter.config.GrpcProperties;
import fast.cloud.nacos.grpc.starter.service.CommonService;
import fast.cloud.nacos.grpc.starter.util.NetUtils;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.Optional;

/**
 * gRPC Server
 */
@Slf4j
public class GrpcServer implements DisposableBean {

    private final GrpcProperties grpcProperties;

    private final CommonService commonService;

    private ServerInterceptor serverInterceptor;

    private Server server;

    private NamingService namingService;

    public GrpcServer(GrpcProperties grpcProperties, CommonService commonService, NamingService namingService) {
        this.grpcProperties = grpcProperties;
        this.commonService = commonService;
        this.namingService = namingService;

    }

    public GrpcServer(GrpcProperties grpcProperties, CommonService commonService, ServerInterceptor serverInterceptor) {
        this.grpcProperties = grpcProperties;
        this.commonService = commonService;
        this.serverInterceptor = serverInterceptor;
    }

    /**
     * 启动服务
     * @throws Exception 异常
     */
    public void start() throws Exception{
        int port = grpcProperties.getPort();
        if (serverInterceptor != null){
            server = ServerBuilder.forPort(port).addService(ServerInterceptors.intercept(commonService, serverInterceptor)).build().start();
        }else {
            Class clazz = grpcProperties.getServerInterceptor();
            if (clazz == null){
                server = ServerBuilder.forPort(port).addService(commonService).build().start();
            }else {
                server = ServerBuilder.forPort(port).addService(ServerInterceptors.intercept(commonService, (ServerInterceptor) clazz.newInstance())).build().start();
            }
        }
        log.info("gRPC Server started, listening on port " + server.getPort());
        startDaemonAwaitThread();
        //注册到注册中心
        String grpcServerName = grpcProperties.getGrpcServerName();
        //抽象注册中心
        namingService.registerInstance(grpcServerName, NetUtils.getLocalHost(),port);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        log.info("gRPC server stopped.");
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(()->{
            try {
                GrpcServer.this.server.awaitTermination();
            } catch (InterruptedException e) {
                log.warn("gRPC server stopped." + e.getMessage());
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

}