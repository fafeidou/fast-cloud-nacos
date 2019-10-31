package fast.cloud.nacos.consumer.fallback.sentinel.util;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;


public class ExceptionUtil {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static SentinelClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
        logger.error(ex.getMessage(), ex);
        return new SentinelClientHttpResponse("RestTemplate FallBack Msg");
    }
}
