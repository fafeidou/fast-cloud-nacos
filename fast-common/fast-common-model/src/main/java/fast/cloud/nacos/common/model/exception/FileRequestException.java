package fast.cloud.nacos.common.model.exception;

@SuppressWarnings("serial")
public class FileRequestException extends RuntimeException {

    public FileRequestException() {
        super();
    }

    public FileRequestException(String code) {
        super(code);
    }

    public FileRequestException(String code, Throwable cause) {
        super(code, cause);
    }

    public FileRequestException(Throwable cause) {
        super(cause);
    }
}
