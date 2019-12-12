package fast.cloud.nacos.securityauth.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by admin on 2018/3/19.
 */
@Data
@ToString
public class FooMenu {

    private String id;
    private String code;
    private String pCode;
    private String pId;
    private String menuName;
    private String url;
    private String isMenu;
    private Integer level;
    private Integer sort;
    private String status;
    private String icon;
    private Date createTime;
    private Date updateTime;


}
