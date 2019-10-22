package fast.cloud.nacos.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoEntity> implements DemoService {
}
