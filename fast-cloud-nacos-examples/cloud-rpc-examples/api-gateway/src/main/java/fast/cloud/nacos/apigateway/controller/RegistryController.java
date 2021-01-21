package fast.cloud.nacos.apigateway.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author qinfuxiang
 * @since 2020/11/12 18:47
 */
@RestController
@RequestMapping(value = "registry")
public class RegistryController {

    protected Logger logger = LoggerFactory.getLogger(RegistryController.class.getSimpleName());

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;


    @GetMapping("/listAll")
    public Mono<String> listAll(ServerWebExchange exchange) {
        return Mono.just(JSON.toJSONString(getAll()));
    }

    @GetMapping("/list")
    public Mono<String> list(ServerWebExchange exchange) {
        //获取各服务注册信息
        Map<String, List<ServiceInstance>> gatewayInstanceMap = new HashMap<>();
        discoveryClient.getServices().forEach(service -> {
            List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(service);
            if (serviceInstanceList != null) {
                gatewayInstanceMap.put(service, serviceInstanceList);
            }
        });
        return Mono.just(JSON.toJSONString(gatewayInstanceMap));
    }

    @GetMapping("/get")
    public Mono<String> get(ServerWebExchange exchange, @RequestParam("serviceName") String serviceName) {
        try {
            Map<String, List<ServiceInstance>> allMap = getAll();
            return Mono.just(JSON.toJSONString(allMap.get(serviceName)));
        } catch (Exception e) {
            logger.info("get exception, e=", e);
            return Mono.empty();
        }
    }

    @GetMapping("/deregisterInstance")
    public String shutDown() throws NacosException {
        nacosDiscoveryProperties.namingServiceInstance().deregisterInstance("api-gateway", "10.192.233.21", 18085);
        return "deregisterInstance";
    }

    private Map<String, List<ServiceInstance>> getAll() {
        Map<String, List<ServiceInstance>> allInstanceMap = new HashMap<>();
        List<String> allServiceNames = discoveryClient.getServices();
        for (String serviceName : allServiceNames) {
            List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);
            if (serviceInstanceList != null) {
                allInstanceMap.put(serviceName, serviceInstanceList);
            }
        }
        return allInstanceMap;
    }
}

