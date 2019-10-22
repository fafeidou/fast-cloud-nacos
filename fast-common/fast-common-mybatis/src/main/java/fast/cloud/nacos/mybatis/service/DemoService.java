package fast.cloud.nacos.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fast.cloud.nacos.common.model.response.PaginationResponse;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;
import fast.cloud.nacos.mybatis.condition.DemoCondition;
import fast.cloud.nacos.mybatis.entity.DemoEntity;

public interface DemoService extends IService<DemoEntity> {
    /**
     * 分页查询
     */
    PaginationResponse<DemoEntity> findDemoPage(MyBaseRequest<DemoCondition> request);
}
