package fast.cloud.nacos.horizontal.subdivision;

import fast.cloud.nacos.horizontal.subdivision.dao.OrderDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {FastCommonShardingHorizontalSubdivisionApplication.class})
public class FastCommonShardingHorizontalSubdivisionApplicationTests {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testInsertOrder() {
        for (int i = 0; i < 10; i++) {
            orderDao.insertOrder(new BigDecimal((i + 1) * 5), 1L, "WAIT_PAY");
        }
        for (int i = 0; i < 10; i++) {
            orderDao.insertOrder(new BigDecimal((i + 1) * 10), 2L, "WAIT_PAY");
        }
    }

    @Test
    public void testSelectOrderbyIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(448193675273437184L);
        ids.add(448193676187795456L);

        List<Map> maps = orderDao.selectOrderbyIds(ids);
        System.out.println(maps);
    }

    @Test
    public void testSelectOrderbyUserAndIds() {
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(448193675273437184L);
        orderIds.add(448193676187795456L);
        //查询条件中包括分库的键user_id
        int user_id = 2;
        List<Map> orders = orderDao.selectOrderbyUserAndIds(user_id, orderIds);
    }

}
