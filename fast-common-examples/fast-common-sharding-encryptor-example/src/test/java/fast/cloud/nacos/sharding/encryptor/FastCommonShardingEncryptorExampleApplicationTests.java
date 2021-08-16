package fast.cloud.nacos.sharding.encryptor;

import fast.cloud.nacos.sharding.encryptor.dao.UserDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = FastCommonShardingEncryptorExampleApplication.class)
public class FastCommonShardingEncryptorExampleApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void testInsertUser() {
        for (int i = 20; i < 30; i++) {
            Long id = i + 1L;
            userDao.insertUser(id, "姓名" + id);
        }
    }

    @Test
    public void testSelectUserbyIds() {
        List<Long> userIds = new ArrayList<>();
        userIds.add(21L);
        userIds.add(22L);
        List<Map> users = userDao.selectUserbyIds(userIds);
        System.out.println(users);
    }

}
