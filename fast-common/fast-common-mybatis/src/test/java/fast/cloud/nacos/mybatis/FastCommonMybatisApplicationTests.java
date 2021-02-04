package fast.cloud.nacos.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.common.model.request.CommonSearchRequest;
import fast.cloud.nacos.common.model.response.PaginationResponse;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;
import fast.cloud.nacos.mybatis.condition.DemoCondition;
import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.enums.GradeEnum;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FastCommonMybatisApplicationTests {
    @Autowired
    private DemoService demoService;
    @Autowired
    private DemoMapper demoMapper;

    @Test
    public void testSelect1() {
        MyBaseRequest<DemoCondition> request = new MyBaseRequest<>();
        DemoCondition demoCondition = new DemoCondition();
        demoCondition.setName("12");
        request.setCondition(demoCondition);
        PaginationResponse<DemoEntity> page = demoService.findPage(request);
    }

    @Test
    public void testSelect() {
        MyBaseRequest<DemoCondition> request = new MyBaseRequest<>();
        DemoCondition demoCondition = new DemoCondition();
        request.setCondition(demoCondition);
        CommonSearchRequest.Sort sort = new CommonSearchRequest.Sort();
        sort.setDirection(1);
        sort.setField("name");
        request.setSortBy(sort);
        Page<DemoEntity> demoEntityPage = demoService.initPage(request);
        List<DemoEntity> demoEntities = demoMapper.selectDemoPage(demoEntityPage);
        log.info("demoEntities:{}", demoEntities);
    }

    @Test
    public void testInsert() {
        DemoEntity entity = new DemoEntity();
        entity.setId(1L);
        entity.setGrade(GradeEnum.HIGH);
        entity.setName("123123");
        demoMapper.insert(entity);
    }

}
