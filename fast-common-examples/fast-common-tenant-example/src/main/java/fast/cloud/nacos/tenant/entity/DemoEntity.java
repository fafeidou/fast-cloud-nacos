package fast.cloud.nacos.tenant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("demo")
public class DemoEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
