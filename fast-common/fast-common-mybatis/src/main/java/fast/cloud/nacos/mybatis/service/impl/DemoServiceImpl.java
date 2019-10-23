package fast.cloud.nacos.mybatis.service.impl;

import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends BaseService<DemoMapper, DemoEntity> implements DemoService {

}
