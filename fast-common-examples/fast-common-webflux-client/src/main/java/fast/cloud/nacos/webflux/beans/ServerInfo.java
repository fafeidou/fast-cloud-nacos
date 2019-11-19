package fast.cloud.nacos.webflux.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器信息类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {

	/**
	 * 服务器url
	 */
	private String url;

	private String microName;

}
