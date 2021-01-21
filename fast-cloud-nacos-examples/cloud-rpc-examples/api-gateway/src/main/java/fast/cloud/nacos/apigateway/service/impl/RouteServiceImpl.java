package fast.cloud.nacos.apigateway.service.impl;

import fast.cloud.nacos.apigateway.model.MyRoute;
import fast.cloud.nacos.apigateway.model.RouteDetail;
import fast.cloud.nacos.apigateway.model.RouteInfo;
import fast.cloud.nacos.apigateway.route.DynamicRouteServiceImplByNacos;
import fast.cloud.nacos.apigateway.service.RouteService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author qinfuxiang
 * @since 2020/11/13 9:35
 */
public class RouteServiceImpl implements RouteService {

    protected Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class.getSimpleName());

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Autowired
    private DynamicRouteServiceImplByNacos dynamicRouteServiceImplByNacos;

    @Override
    public MyRoute list() {
        MyRoute route = new MyRoute();
        List<RouteDetail> routeDetailList = new ArrayList<>();

        List<RouteDefinition> routeDefinitionList = listDefinition();
        routeDefinitionList.forEach(routeDefinition -> {
            RouteDetail routeDetail = new RouteDetail();
            routeDetail.setRouteDefinition(routeDefinition);
            routeDetailList.add(routeDetail);
        });
        return route;
    }

    @Override
    public List<RouteDetail> listDetail() {
        List<RouteDetail> routeDetailList = new ArrayList<>();

        List<RouteDefinition> routeDefinitionList = listDefinition();
        routeDefinitionList.forEach(routeDefinition -> {
            RouteDetail routeDetail = new RouteDetail();
            routeDetail.setRouteDefinition(routeDefinition);
            routeDetailList.add(routeDetail);
        });
        return routeDetailList;
    }

    @Override
    public RouteInfo getRoute(String routeId) {
        //        List<RouteInfo> routeInfoList = list();
        //        for (RouteInfo routeInfo : routeInfoList) {
        //            if (routeInfo.getRouteId().equals(routeId)) {
        //                return routeInfo;
        //            }
        //        };
        return null;
    }

    @Override
    public RouteDetail getRouteDetail(String routeId) {
        RouteDetail routeDetail = new RouteDetail();
        RouteInfo routeInfo = getRoute(routeId);
        routeDetail.setRouteInfo(routeInfo);
        List<RouteDefinition> routeDefinitionList = listDefinition();
        for (RouteDefinition routeDefinition : routeDefinitionList) {
            if (routeDefinition.getId().equals(routeInfo.getRouteId())) {
                routeDetail.setRouteDefinition(routeDefinition);
                break;
            }
        }
        return routeDetail;
    }

    @Override
    public boolean addRoute(RouteInfo routeInfo) {
        return true;
    }

    @Override
    public boolean updateRoute(RouteInfo routeInfo) {
        return true;
    }

    @Override
    public boolean deleteRoute(String serviceName) {
        return true;
    }

    @Override
    public List<RouteDefinition> listDefinition() {
        Flux<RouteDefinition> routeDefinitionFlux = routeDefinitionLocator.getRouteDefinitions();
        Mono<List<RouteDefinition>> routeDefinitionListMono = routeDefinitionFlux.collectList();
        List<RouteDefinition> result = new ArrayList<>();
        routeDefinitionListMono.subscribe(routeDefinitionList -> {
            result.addAll(routeDefinitionList);
        });

        return result;
    }

}
