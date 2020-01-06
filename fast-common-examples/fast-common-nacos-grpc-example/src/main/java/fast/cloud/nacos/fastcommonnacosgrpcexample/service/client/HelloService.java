package fast.cloud.nacos.fastcommonnacosgrpcexample.service.client;

import fast.cloud.nacos.fastcommonnacosgrpcexample.HelloWorldClient;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcNacosOptions;
import fast.cloud.nacos.fastcommonnacosgrpcexample.grpc.GrpcNacosProto;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class HelloService {
    public void hello() {
        int port = GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.grpcNacosPort);
        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
        HelloWorldClient client = new HelloWorldClient(port, uri, "GrpcTestService");
        client.reqString("AAA");
    }
}
