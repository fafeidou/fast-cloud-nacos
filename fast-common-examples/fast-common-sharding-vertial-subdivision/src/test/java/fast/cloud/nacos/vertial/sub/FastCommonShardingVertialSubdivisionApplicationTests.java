package fast.cloud.nacos.vertial.sub;

import fast.cloud.nacos.vertial.sub.dao.DictDao;
import fast.cloud.nacos.vertial.sub.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {FastCommonShardingVertialSubdivisionApplication.class})
public class FastCommonShardingVertialSubdivisionApplicationTests {
    @Autowired
    private UserDao userDao;

    @Autowired
    private DictDao dictDao;

    @Test
    public void testInsertUser(){
        for (int i = 0 ; i<10; i++){
            Long id = i + 1L;
            userDao.insertUser(id,"姓名"+ id );
        }
    }

    @Test
    public void testSelectUserbyIds(){
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        userIds.add(2L);
        List<Map> users = userDao.selectUserbyIds(userIds);
        System.out.println(users);
    }

    @Test
    public void testInsertDict(){
        dictDao.insertDict(1L,"user_type","0","管理员");
        dictDao.insertDict(2L,"user_type","1","操作员");
    }

    @Test
    public void testDeleteDict(){
        dictDao.deleteDict(1L);
        dictDao.deleteDict(2L);
    }
}
