package fast.cloud.rocketmq.entity;

import lombok.Data;

@Data
public class AccountInfo implements java.io.Serializable {
	private Long id;
	private String accountName;
	private String accountNo;
	private String accountPassword;
	private Double accountBalance;
}
