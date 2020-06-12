package fast.cloud.nacos.provider.service;


import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceByJackon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceByJackonImpl implements UserServiceByJackon {

    /**
     * 模拟数据库存储用户信息
     */
    private Map<Long, UserEntity> userMap = new ConcurrentHashMap<>();

    @Override
    public List<UserEntity> findAll() {
        Collection<UserEntity> values = userMap.values();
        return new ArrayList<>(values);
    }

}
