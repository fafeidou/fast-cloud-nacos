package fast.cloud.nacos.apigateway.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author qinfuxiang
 * @since 2020/11/13 9:37
 */
public class RouteDetail implements Serializable {
    private RouteInfo routeInfo;

    private RouteDefinition routeDefinition;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public RouteInfo getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(RouteInfo routeInfo) {
        this.routeInfo = routeInfo;
    }

    public RouteDefinition getRouteDefinition() {
        return routeDefinition;
    }

    public void setRouteDefinition(RouteDefinition routeDefinition) {
        this.routeDefinition = routeDefinition;
    }

}
