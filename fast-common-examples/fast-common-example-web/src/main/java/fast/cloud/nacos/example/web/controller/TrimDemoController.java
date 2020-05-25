package fast.cloud.nacos.example.web.controller;

import fast.cloud.nacos.common.model.utils.GsonUtil;
import fast.cloud.nacos.example.web.bean.TrimBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinfuxiang
 * @Date 2020/5/25 17:35
 */
@RestController
@Slf4j
public class TrimDemoController {

    @PostMapping    (value = "/test/trim")
    public void trim(@RequestBody TrimBean trimBean) {
        log.info("trim bean {}:", GsonUtil.toJson(trimBean));
    }
}
