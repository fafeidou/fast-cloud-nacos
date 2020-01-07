package fast.cloud.nacos.fastcommonnacosgrpcexample;

import com.alibaba.nacos.api.exception.NacosException;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcNacosOptions;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcNacosProto;
import fast.cloud.nacos.fastcommonnacosgrpcexample.server.GrpcServer;
import fast.cloud.nacos.fastcommonnacosgrpcexample.service.GrpcTestServiceImpl;
import fast.cloud.nacos.fastcommonnacosgrpcexample.utils.NacosUtils;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

public class HelloWorldServer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    GrpcServer server;

    private void start(BindableService[] bindableServices) throws NacosException {
        server = new GrpcServer();

        int port = GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.grpcNacosPort);
        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
        Properties properties = new Properties();
        properties.setProperty("serviceName", "demo");
        properties = NacosUtils.buildNacosProperties(uri, properties);
        server.init(50051, properties, bindableServices);
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            server.stop();
            System.err.println("*** server shut down");
        }));

    }

    public static void main(String[] args) throws IOException, InterruptedException, NacosException {
        final HelloWorldServer server = new HelloWorldServer();
        server.start(new GrpcTestServiceImpl[]{new GrpcTestServiceImpl()});
        server.server.blockUtilShutdown();
    }

}
