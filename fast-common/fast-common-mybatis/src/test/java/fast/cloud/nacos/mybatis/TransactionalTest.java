package fast.cloud.nacos.mybatis;

import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname TransactionalTest
 * @Description TODO
 * @Date 2020/3/26 13:40
 * @Created by qinfuxiang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TransactionalTest {
    @Autowired
    DemoService demoService;

    @Test
    public void testExample1() {
        DemoEntity user = new DemoEntity();
        user.setName("batman");
        demoService.Example1(user);
    }

    @Test
    public void testExample2() {
        DemoEntity user = new DemoEntity();
        user.setName("batman");
        demoService.Example2(user);
    }

    @Test
    public void testExample3() {
        DemoEntity user = new DemoEntity();
        user.setName("batman");
        demoService.Example3(user);
    }

    @Test
    public void testExample4() {
        DemoEntity user = new DemoEntity();
        user.setName("batman");
        demoService.Example4(user);
    }


}
