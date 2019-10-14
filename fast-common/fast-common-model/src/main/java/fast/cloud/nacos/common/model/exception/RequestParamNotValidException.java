package fast.cloud.nacos.common.model.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API请求参数的业务验证不通过抛出此异常，由GlobalExceptionHandler做统一返回结果处理。
 */
public class RequestParamNotValidException extends RuntimeException {

	private final BindingResult bindingResult;

	/**
	 * custom message
	 */
	private String message;

	public RequestParamNotValidException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
		List<String> messages = bindingResult.getAllErrors().stream()
				.map(ObjectError::getCode).collect(Collectors.toList());
		this.message = StringUtils.join(messages, ",");
	}

	public BindingResult getBindingResult() {
		return this.bindingResult;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * use custom message, if any
	 *
	 * @return
	 */
	@Override
	public String getMessage() {

		if (StringUtils.isEmpty(message)) {
			return super.getMessage();
		}
		return message;
	}

}
