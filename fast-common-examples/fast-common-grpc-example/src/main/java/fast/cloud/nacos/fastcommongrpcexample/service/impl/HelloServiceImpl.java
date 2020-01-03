package fast.cloud.nacos.fastcommongrpcexample.service.impl;

import fast.cloud.nacos.fastcommongrpcexample.grpc.SimpleClient;
import fast.cloud.nacos.fastcommongrpcexample.service.IHelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements IHelloService {
    private Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Value("${gRPC.host}")
    private String host;
    @Value("${gRPC.port}")
    private int port;

    @Override
    public String sayHello(String name) {
        SimpleClient client = new SimpleClient(host, port);
        String s = client.myHello(name);
        try {
            client.shutdown();
        } catch (InterruptedException e) {
            logger.error("channel关闭异常：err={}", e.getMessage());
        }
        return s;
    }

}
