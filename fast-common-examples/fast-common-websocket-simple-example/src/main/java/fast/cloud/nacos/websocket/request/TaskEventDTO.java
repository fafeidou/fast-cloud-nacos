package fast.cloud.nacos.websocket.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author qinfuxiang
 * @Classname TaskEventDTO
 * @Description TODO
 * @Date 2020/4/8 15:00
 */
@Data
@ToString
public class TaskEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private Long stationId;

    private Long msgId;
}
