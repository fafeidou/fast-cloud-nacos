package fast.cloud.nacos.tenant.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.tenant.dynamic.mybatis.DataSource;
import fast.cloud.nacos.tenant.entity.TenantDemoEntity;
import fast.cloud.nacos.tenant.enums.DS;

import java.util.List;

//@Mapper
@DataSource(DS.TENANT_MANAGE)
public interface TenantDemoMapper {

    @DataSource(DS.TENANT_MANAGE)
    List<TenantDemoEntity> selectTenantDemoPage(Page<TenantDemoEntity> page);
}
