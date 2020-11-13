package fast.cloud.nacos.apigateway.model;

import lombok.Data;

/**
 * @author qinfuxiang
 * @since 2020/11/13 9:42
 */
@Data
public class RouteInfo {

    private Long id;                //主键

    private String routeId;         //路由id

    private String predicateType;   //断言类型：Host, Path, etc

    private String predicateParam;  //断言参数

    private String uri;             //转发uri

    private Integer stripPrefix;    //strip prefix

    private Integer order;          //order

}
