package fast.cloud.nacos.orderservice.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Classname Order
 * @Description TODO
 * @Date 2020/4/4 21:01
 * @Created by qinfuxiang
 */
@Data
@Accessors(chain = true)
@TableName("order_tbl")
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userId;
    private String commodityCode;
    private Integer count;
    private BigDecimal money;

}