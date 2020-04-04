package fast.cloud.nacos.storageservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname Storage
 * @Description TODO
 * @Date 2020/4/4 21:14
 * @Created by qinfuxiang
 */
@Data
@Accessors(chain = true)
@TableName("storage_tbl")
public class Storage {

    private Long id;
    private String commodityCode;
    private Long count;


}