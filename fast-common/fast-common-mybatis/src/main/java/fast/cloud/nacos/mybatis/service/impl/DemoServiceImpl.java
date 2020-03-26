package fast.cloud.nacos.mybatis.service.impl;

import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import fast.cloud.nacos.mybatis.service.PropagationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoServiceImpl extends BaseService<DemoMapper, DemoEntity> implements DemoService {
    @Autowired
    private PropagationService propagationService;

    @Autowired
    private DemoMapper demoMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void Example1(DemoEntity demoEntity) {
        demoMapper.insert(demoEntity);
        propagationService.required();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void Example2(DemoEntity demoEntity) {
        demoMapper.insert(demoEntity);
        try {
            propagationService.required();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void Example3(DemoEntity demoEntity) {
        demoMapper.insert(demoEntity);
        try {
            propagationService.requiresNew();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void Example4(DemoEntity demoEntity) {
        demoMapper.insert(demoEntity);
        propagationService.requiresNew();
    }
}
