package fast.cloud.nacos.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fast.cloud.nacos.mybatis.entity.DemoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemoMapper extends BaseMapper<DemoEntity> {
    List<DemoEntity> selectDemoPage(Page<DemoEntity> page);
}
