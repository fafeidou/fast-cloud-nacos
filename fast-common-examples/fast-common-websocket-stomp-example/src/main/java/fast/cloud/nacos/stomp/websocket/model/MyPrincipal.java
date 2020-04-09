package fast.cloud.nacos.stomp.websocket.model;

import java.security.Principal;

/**
 * @Classname MyPrincipal
 * @Description TODO
 * @Date 2020/4/9 15:08
 * @Created by qinfuxiang
 */
public class MyPrincipal implements Principal {
    private String loginName;

    public MyPrincipal(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getName() {
        return loginName;
    }
}
