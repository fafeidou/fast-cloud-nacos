package fast.cloud.nacos.mybatis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import fast.cloud.nacos.common.model.condition.PageCondition;
import fast.cloud.nacos.common.model.response.PaginationResponse;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2019-01-03 12:45
 */
public interface IBaseService<T> extends IService<T> {
    <CONDITION extends PageCondition> PaginationResponse<T> findPage(MyBaseRequest<CONDITION> request);
}
