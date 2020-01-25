package fast.cloud.nacos.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.tenant.dynamic.mybatis.DataSource;
import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.enums.DS;

import java.util.List;

//@Mapper
@DataSource(DS.MY_CAT)
public interface DemoMapper extends BaseMapper<DemoEntity> {
    List<DemoEntity> selectDemoPage(Page<DemoEntity> page);
}
