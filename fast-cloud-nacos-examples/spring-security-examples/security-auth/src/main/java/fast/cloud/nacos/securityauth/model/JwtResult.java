package fast.cloud.nacos.securityauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by mrt on 2018/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class JwtResult {
    private String jwt;
}
