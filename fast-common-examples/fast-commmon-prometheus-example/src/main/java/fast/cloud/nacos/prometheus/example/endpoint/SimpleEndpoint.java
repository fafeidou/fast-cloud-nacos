package fast.cloud.nacos.prometheus.example.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

@Endpoint(id = "simple")
public class SimpleEndpoint {


    @ReadOperation
    public String getHello(){
        return "get Hello";
    }
    @WriteOperation
    public String postHello(){
        return "post Hello";
    }
    @DeleteOperation
    public String deleteHello(){
        return "delete Hello";
    }
}