package fast.cloud.nacos.storageservice.controller;

import fast.cloud.nacos.storageservice.service.StorageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname StorageController
 * @Description TODO
 * @Date 2020/4/4 21:18
 * @Created by qinfuxiang
 */
@RestController
@RequestMapping("storage")
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
    @RequestMapping(path = "/deduct")
    public Boolean deduct(String commodityCode, Integer count) {
        storageService.deduct(commodityCode, count);
        return true;
    }

}