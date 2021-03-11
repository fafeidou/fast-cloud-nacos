package fast.cloud.nacos.controller;

import fast.cloud.nacos.service.StorageService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname StorageController
 * @Description TODO
 * @Date 2020/4/4 21:18
 * @Created by qinfuxiang
 */
@RestController
public class StorageController {

    @Resource
    private StorageService storageService;

    /**
     * 减库存
     *
     * @param commodityCode 商品代码
     * @param count         数量
     * @return
     */
    @GetMapping("storage/deduct")
    Boolean deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count) {
        storageService.deduct(commodityCode, count);
        return true;
    }

}