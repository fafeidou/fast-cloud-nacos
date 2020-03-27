package fast.cloud.nacos.sharding.encryptor.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Classname UserDao
 * @Description TODO
 * @Date 2020/3/21 20:20
 * @Created by qinfuxiang
 */
@Mapper
@Component
public interface UserDao {
    /**
     * 新增用户
     * @param userId 用户id
     * @param fullname 用户姓名
     * @return
     */
    @Insert("insert into t_user(user_id, fullname) value(#{userId},#{fullname})")
    int insertUser(@Param("userId") Long userId, @Param("fullname") String fullname);
    /**
     * 根据id列表查询多个用户
     * @param userIds 用户id列表
     * @return
     */
    @Select({"<script>",
            " select",
            " * ",
            " from t_user t ",
            " where t.user_id in",
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Map> selectUserbyIds(@Param("userIds") List<Long> userIds);
}
