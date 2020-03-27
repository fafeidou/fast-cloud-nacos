package fast.cloud.nacos.sharding.encryptor.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Classname DictDao
 * @Description TODO
 * @Date 2020/3/21 20:35
 * @Created by qinfuxiang
 */
@Mapper
@Component
public interface DictDao {
    /**
     * 新增字典
     *
     * @param type  字典类型
     * @param code  字典编码
     * @param value 字典值
     * @return
     */
    @Insert("insert into t_dict(dict_id,type,code,value) value(#{dictId},#{type},#{code},#{value})")
    int insertDict(@Param("dictId") Long dictId, @Param("type") String type, @Param("code") String
            code, @Param("value") String value);

    /**
     * 删除字典
     *
     * @param dictId 字典id
     * @return
     */
    @Delete("delete from t_dict where dict_id = #{dictId}")
    int deleteDict(@Param("dictId") Long dictId);
}
