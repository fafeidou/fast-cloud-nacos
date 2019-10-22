package fast.cloud.nacos.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fast.cloud.nacos.common.model.response.PaginationResponse;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;
import fast.cloud.nacos.mybatis.condition.DemoCondition;
import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoEntity> implements DemoService {

    @Override
    public PaginationResponse<DemoEntity> findDemoPage(MyBaseRequest<DemoCondition> request) {
        QueryWrapper queryWrapper = request.getQuery(DemoEntity.class);

        Page<DemoEntity> page = new Page<>();
        page.setCurrent(request.getCondition().getPage());
        page.setSize(request.getCondition().getSize());

        IPage<DemoEntity> result = baseMapper.selectPage(page, queryWrapper);

        PaginationResponse<DemoEntity> paginationResponse = new PaginationResponse<>();
        paginationResponse.setDetails(result.getRecords());
        paginationResponse.setTotal(result.getTotal());
        return paginationResponse;
    }
}
