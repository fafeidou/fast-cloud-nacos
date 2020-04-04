package fast.cloud.nacos.storageservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fast.cloud.nacos.storageservice.entity.Storage;
import fast.cloud.nacos.storageservice.repository.StorageDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Classname StorageService
 * @Description TODO
 * @Date 2020/4/4 21:17
 * @Created by qinfuxiang
 */
@Service
public class StorageService {

    @Resource
    private StorageDAO storageDAO;

    /**
     * 减库存
     *
     * @param commodityCode
     * @param count
     */
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        if (commodityCode.equals("product-2")) {
            throw new RuntimeException("异常:模拟业务异常:Storage branch exception");
        }

        QueryWrapper<Storage> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Storage().setCommodityCode(commodityCode));
        Storage storage = storageDAO.selectOne(wrapper);
        storage.setCount(storage.getCount() - count);

        storageDAO.updateById(storage);
    }
}