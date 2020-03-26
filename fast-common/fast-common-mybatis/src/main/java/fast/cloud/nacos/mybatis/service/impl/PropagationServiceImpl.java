package fast.cloud.nacos.mybatis.service.impl;

import fast.cloud.nacos.mybatis.service.PropagationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname PropagationServiceImpl
 * @Description TODO
 * @Date 2020/3/26 13:47
 * @Created by qinfuxiang
 */
@Service
public class PropagationServiceImpl implements PropagationService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void required() {
        throw new NullPointerException("假装抛出了异常");
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void requiresNew() {
        throw new NullPointerException("假装抛出了异常");
    }
}
