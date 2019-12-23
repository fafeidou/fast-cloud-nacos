package fast.cloud.nacos.securityauth.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UserJwt extends User {

    private String id;
    private String name;
    private String userpic;
    private String utype;
    private String companyId;

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
