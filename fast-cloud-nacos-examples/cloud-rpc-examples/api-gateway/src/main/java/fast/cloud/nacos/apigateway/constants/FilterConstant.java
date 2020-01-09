package fast.cloud.nacos.apigateway.constants;

import lombok.Data;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.stereotype.Component;

@Data
@Component
public class FilterConstant {

    public static final String CACHED_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";
    public static final String CACHED_RESPONSE_BODY_OBJECT_KEY = "cachedResponseBodyObject";

    public static final int LOGGER_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER;

    public static final int CACHE_REQUEST_BODY_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1;

    public static final int READ_REQUEST_BODY_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 2;

    public static final int CACHE_RESPONSE_BODY_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER - 1;

    public static final int READ_RESPONSE_BODY_FILTER_ORDER = RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER - 2;

}
