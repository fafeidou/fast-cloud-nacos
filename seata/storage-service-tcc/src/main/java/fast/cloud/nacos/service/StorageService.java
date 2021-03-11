package fast.cloud.nacos.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fast.cloud.nacos.entity.Storage;
import fast.cloud.nacos.tcc.StorageTccAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname StorageService
 * @Description TODO
 * @Date 2020/4/4 21:17
 * @Created by qinfuxiang
 */
@Service
public class StorageService {

    @Autowired
    private StorageTccAction storageTccAction;

    /**
     * 减库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        if (commodityCode.equals("product-2")) {
            throw new RuntimeException("异常:模拟业务异常:Storage branch exception");
        }
        storageTccAction.prepareDecreaseStorage(null, commodityCode, count);
    }
}