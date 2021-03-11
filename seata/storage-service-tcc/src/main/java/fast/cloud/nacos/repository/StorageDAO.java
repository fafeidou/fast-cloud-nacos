package fast.cloud.nacos.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fast.cloud.nacos.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Classname StorageDAO
 * @Description TODO
 * @Date 2020/4/4 21:16
 * @Created by qinfuxiang
 */
@Mapper
@Repository
public interface StorageDAO extends BaseMapper<Storage> {

    void decrease(String productId, Integer count);

    void updateFrozen(@Param("productId") String productId, @Param("residue") Integer residue, @Param("frozen") Integer frozen);

    void updateFrozenToUsed(@Param("productId") String productId, @Param("count") Integer count);

    void updateFrozenToResidue(@Param("productId") String productId, @Param("count") Integer count);

    Storage selectByProductId(@Param("productId") String productId);
}
