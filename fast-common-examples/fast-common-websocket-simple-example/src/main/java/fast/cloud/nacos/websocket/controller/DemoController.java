package fast.cloud.nacos.websocket.controller;

import fast.cloud.nacos.websocket.config.WebSocketServer;
import fast.cloud.nacos.websocket.request.TaskEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname DemoController
 * @Description TODO
 * @Date 2020/4/8 15:03
 * @Created by qinfuxiang
 */
@RestController
@RequestMapping("demo")
public class DemoController {
    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping("send")
    public String send(@RequestBody TaskEventDTO taskEventDTO) {
        webSocketServer.sendMessage(taskEventDTO.getUserId(), taskEventDTO);
        return "ok";
    }
}
