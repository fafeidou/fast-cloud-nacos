package fast.cloud.nacos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname Storage
 * @Description TODO
 * @Date 2020/4/4 21:14
 * @Created by qinfuxiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Storage {

    private Long id;
    private String productId;
    private Integer total;
    private Integer used;
    private Integer residue;
    private Integer frozen;
}