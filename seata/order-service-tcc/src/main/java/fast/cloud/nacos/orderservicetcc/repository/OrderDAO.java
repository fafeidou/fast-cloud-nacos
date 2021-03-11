package fast.cloud.nacos.orderservicetcc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fast.cloud.nacos.orderservicetcc.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Classname OrderDAO
 * @Description TODO
 * @Date 2020/4/4 21:02
 * @Created by qinfuxiang
 */
@Mapper
@Repository
public interface OrderDAO extends BaseMapper<Order> {

    void updateStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

}
