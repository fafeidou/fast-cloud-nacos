package fast.cloud.nacos.provider.service;

import fast.cloud.nacos.grpc.api.entity.UserEntity;
import fast.cloud.nacos.grpc.api.service.UserServiceByProtoStuff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * service 只是交给spring代理，如果没有本地调用，可以去掉
 */
@Service
@Slf4j
public class UserServiceByProtoStuffImpl implements UserServiceByProtoStuff {

    /**
     * 模拟数据库存储用户信息
     */
    private Map<Long, UserEntity> userMap = new ConcurrentHashMap<>();

    @Override
    public void insert(UserEntity userEntity) {
        if (userEntity == null){
            log.warn("insert user fail, userEntity is null!");
            return ;
        }
        userMap.putIfAbsent(userEntity.getId(), userEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null){
            log.warn("delete user fail, id is null!");
        }
        userMap.remove(id);
    }

    @Override
    public List<UserEntity> findAll() {
        Collection<UserEntity> values = userMap.values();
        return new ArrayList<>(values);
    }

}
