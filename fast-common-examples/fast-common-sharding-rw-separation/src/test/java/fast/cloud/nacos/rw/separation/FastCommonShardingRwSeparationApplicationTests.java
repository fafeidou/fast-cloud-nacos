package fast.cloud.nacos.rw.separation;

import fast.cloud.nacos.rw.separation.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = FastCommonShardingRwSeparationApplication.class)
public class FastCommonShardingRwSeparationApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void testInsertUser(){
        for (int i = 10 ; i<20; i++){
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

}
