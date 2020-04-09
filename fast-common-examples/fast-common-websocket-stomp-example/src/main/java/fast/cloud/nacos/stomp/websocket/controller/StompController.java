package fast.cloud.nacos.stomp.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinfuxiang
 */
@RestController
public class StompController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;

    @GetMapping("/test")
    public void test(String username) {
        SimpUser user = userRegistry.getUser(username);
        messagingTemplate.convertAndSend("/topic/" + username, "hello:" + username);
//        messagingTemplate.convertAndSendToUser(username, "/topic/greetings", "hello:"+username);
    }
}
