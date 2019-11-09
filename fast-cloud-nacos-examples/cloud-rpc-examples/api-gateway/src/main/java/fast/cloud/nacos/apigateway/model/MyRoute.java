package fast.cloud.nacos.apigateway.model;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;
import java.util.List;

@Data
public class MyRoute implements Serializable {
    List<RouteDefinition> routeDefinitionList;
}
