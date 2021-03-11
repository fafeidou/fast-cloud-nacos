package fast.cloud.nacos.orderservicetcc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname Order
 * @Date 2020/4/4 21:01
 * @Created by qinfuxiang
 */
@Data
@Accessors(chain = true)
@TableName("order_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String commodityCode;
    private Integer count;
    private BigDecimal money;
    private Integer status;

}