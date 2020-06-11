package fast.cloud.nacos.grpc.api.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 宠物
 */
@Data
public class PetEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    private String type;

    private String name;

    private UserEntity owner;

}
