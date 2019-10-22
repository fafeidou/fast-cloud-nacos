package fast.cloud.nacos.mybatis;

import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastCommonMybatisApplicationTests {
    @Autowired
    private DemoMapper demoMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testInsert() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("!23");
        demoMapper.insert(demoEntity);

    }

}
