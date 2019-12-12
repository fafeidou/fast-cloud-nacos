package fast.cloud.nacos.securityauth.bean;

import fast.cloud.nacos.securityauth.entity.FooMenu;
import fast.cloud.nacos.securityauth.entity.FooUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FooUserExt extends FooUser {
    //权限信息
    private List<FooMenu> permissions;

    //企业信息
    private String companyId;
}
