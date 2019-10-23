package fast.cloud.nacos.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fast.cloud.nacos.common.model.condition.PageCondition;
import fast.cloud.nacos.common.model.response.PaginationResponse;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;


public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    private Class<T> kind;

    public <CONDITION extends PageCondition> PaginationResponse<T> findPage(MyBaseRequest<CONDITION> request) {
        QueryWrapper queryWrapper = request.getQuery(kind);

        IPage<T> page = new Page<>();
        page.setCurrent(request.getCondition().getPage());
        page.setSize(request.getCondition().getSize());

        IPage<T> result = baseMapper.selectPage(page, queryWrapper);

        PaginationResponse<T> paginationResponse = new PaginationResponse<>();
        paginationResponse.setDetails(result.getRecords());
        paginationResponse.setTotal(result.getTotal());
        return paginationResponse;
    }
}
