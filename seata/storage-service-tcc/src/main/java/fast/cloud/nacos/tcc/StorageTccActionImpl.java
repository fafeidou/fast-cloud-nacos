package fast.cloud.nacos.tcc;


import fast.cloud.nacos.entity.Storage;
import fast.cloud.nacos.repository.StorageDAO;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class StorageTccActionImpl implements StorageTccAction {

    @Autowired
    private StorageDAO storageMapper;

    @Transactional
    @Override
    public boolean prepareDecreaseStorage(BusinessActionContext businessActionContext, String productId,
        Integer count) {
        log.info("减少商品库存，第一阶段，锁定减少的库存量，productId=" + productId + "， count=" + count);

        Storage storage = storageMapper.selectByProductId(productId);

        if (storage.getResidue() - count < 0) {
            throw new RuntimeException("库存不足");
        }

        /*
        库存减掉count， 冻结库存增加count
         */
        storageMapper.updateFrozen(productId, storage.getResidue() - count, storage.getFrozen() + count);

        //保存标识
        ResultHolder.setResult(getClass(), businessActionContext.getXid(), "p");
        return true;
    }

    @Transactional
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        String productId = businessActionContext.getActionContext("productId").toString();
        int count = Integer.parseInt(businessActionContext.getActionContext("count").toString());
        log.info("减少商品库存，第二阶段提交，productId=" + productId + "， count=" + count);

        //防止重复提交
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }

        storageMapper.updateFrozenToUsed(productId, count);

        //删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }

    @Transactional
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {

        String productId = businessActionContext.getActionContext("productId").toString();
        int count = Integer.parseInt(businessActionContext.getActionContext("count").toString());
        log.info("减少商品库存，第二阶段，回滚，productId=" + productId + "， count=" + count);

        //防止重复提交
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }

        storageMapper.updateFrozenToResidue(productId, count);

        //删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }
}
