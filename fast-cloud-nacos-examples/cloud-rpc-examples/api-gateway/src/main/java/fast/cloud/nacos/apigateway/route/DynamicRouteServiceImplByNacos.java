package fast.cloud.nacos.apigateway.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import java.util.List;
import java.util.concurrent.Executor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author qinfuxiang
 */
@Component
public class DynamicRouteServiceImplByNacos {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @Getter
    public List<RouteDefinition> routeDefinitions;

    public DynamicRouteServiceImplByNacos() {
        dynamicRouteByNacosListener("test-gateway", "test-group");
    }

    /**
     * 监听Nacos Server下发的动态路由配置
     */
    public void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            ConfigService configService = NacosFactory.createConfigService("localhost:8848");
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {

                    routeDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);

                    if (!CollectionUtils.isEmpty(routeDefinitions)) {
                        routeDefinitions.forEach(i -> dynamicRouteService.update(i));
                    }
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            //todo 提醒:异常自行处理此处省略
        }
    }

}
