package fast.cloud.nacos.mybatis.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.common.model.request.CommonSearchRequest;
import fast.cloud.nacos.mybatis.bean.request.MyBaseRequest;
import fast.cloud.nacos.mybatis.condition.DemoCondition;
import fast.cloud.nacos.mybatis.entity.DemoEntity;
import fast.cloud.nacos.mybatis.mapper.DemoMapper;
import fast.cloud.nacos.mybatis.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DemoController {
    @Autowired
    private DemoService demoService;
    @Autowired
    private DemoMapper demoMapper;

    @RequestMapping("demo")
    public void demo() {
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
}
