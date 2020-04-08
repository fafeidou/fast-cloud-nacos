package fast.cloud.nacos.websocket.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname WebSocketReq
 * @Description TODO
 * @Date 2020/4/8 14:48
 * @Created by qinfuxiang
 */
@Data
public class WebSocketReq implements Serializable {
    private String userId;

    private List<Long> stationIds;
}
