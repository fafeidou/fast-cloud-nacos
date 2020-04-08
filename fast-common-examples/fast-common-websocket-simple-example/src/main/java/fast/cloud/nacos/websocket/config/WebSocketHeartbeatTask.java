package fast.cloud.nacos.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 服务器定时推送任务
 *
 * 心跳，防止链接中断
 */
@Component
@RequiredArgsConstructor
public class WebSocketHeartbeatTask {

    private final WebSocketServer webSocketServer;

    @Scheduled(cron = "0/30 * * * * ?")
    public void schedule() {
        webSocketServer.broadCastHeartbeat();
    }

}
