package fast.cloud.nacos.securityauth.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {
    String username;
    String password;
    String verifycode;
}
