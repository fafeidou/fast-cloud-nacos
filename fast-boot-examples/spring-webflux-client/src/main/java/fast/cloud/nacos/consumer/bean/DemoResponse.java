package fast.cloud.nacos.consumer.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class DemoResponse implements Serializable {
    private String memo;
}
