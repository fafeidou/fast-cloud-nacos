package fast.cloud.nacos.common.model.exception;

import java.util.Map;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {

	/**
	 * 错误消息中的变量参数
	 */
	private Map<String, Object> params;

	public BadRequestException() {
		super();
	}

	public BadRequestException(String code) {
		super(code);
	}

	public BadRequestException(String code, Map<String, Object> params) {
		super(code);
		this.params = params;
	}

	public BadRequestException(String code, Throwable cause) {
		super(code, cause);
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
