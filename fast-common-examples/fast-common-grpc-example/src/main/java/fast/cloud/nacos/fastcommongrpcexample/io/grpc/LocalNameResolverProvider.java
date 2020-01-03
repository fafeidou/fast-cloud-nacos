package fast.cloud.nacos.fastcommongrpcexample.io.grpc;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.net.URI;

// 需要实现NameResolverProvider抽象类中的相关方法
public class LocalNameResolverProvider extends NameResolverProvider {
//    private final ConfigInterface configInterface;
//
//    @Autowired
//    public LocalNameResolverProvider(ConfigInterface configInterface) {
//        this.configInterface = configInterface;
//    }

    // 服务是否可用
    @Override
    protected boolean isAvailable() {
        return true;
    }

    // 优先级默认5
    @Override
    protected int priority() {
        return 5;
    }

    // 服务发现类
    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new LocalNameResolver();
    }

    // 服务协议
    @Override
    public String getDefaultScheme() {
        return "localhost";
    }
}


