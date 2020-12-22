package fast.cloud.nacos.rocketmq.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AccountInfoDao {
    //修改账户金额
    @Update("update account_info set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);


   //查询幂等记录，用于幂等控制
    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(String txNo);

    //添加事务记录，用于幂等控制
    @Insert("insert into de_duplication values(#{txNo},now());")
    int addTx(String txNo);

}
