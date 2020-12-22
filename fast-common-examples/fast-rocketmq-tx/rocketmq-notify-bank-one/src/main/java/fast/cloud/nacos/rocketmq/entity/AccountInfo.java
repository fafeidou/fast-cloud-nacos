package fast.cloud.nacos.rocketmq.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class AccountInfo implements Serializable {
	private Long id;
	private String accountName;
	private String accountNo;
	private String accountPassword;
	private Double accountBalance;
}
