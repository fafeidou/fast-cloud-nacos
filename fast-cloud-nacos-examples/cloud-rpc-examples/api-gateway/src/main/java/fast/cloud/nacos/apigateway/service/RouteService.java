package fast.cloud.nacos.apigateway.service;

import fast.cloud.nacos.apigateway.model.MyRoute;
import fast.cloud.nacos.apigateway.model.RouteDetail;
import fast.cloud.nacos.apigateway.model.RouteInfo;
import java.util.List;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author qinfuxiang
 * @since 2020/11/13 9:33
 */
public interface RouteService {

    MyRoute list();

    List<RouteDetail> listDetail();

    RouteInfo getRoute(String serviceName);

    RouteDetail getRouteDetail(String serviceName);

    boolean addRoute(RouteInfo routeInfo);

    boolean updateRoute(RouteInfo routeInfo);

    boolean deleteRoute(String serviceName);

    List<RouteDefinition> listDefinition();

}
