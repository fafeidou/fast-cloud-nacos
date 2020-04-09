package fast.cloud.nacos.stomp.websocket.controller;

import fast.cloud.nacos.stomp.websocket.service.IRedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author qinfuxiang
 */
@RestController
public class StompController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;
    @Autowired
    private IRedisSessionService redisSessionService;

    @GetMapping("/test")
    public void test(String username) {
        SimpUser user = userRegistry.getUser(username);
        //发送之前看是否在线，如果在线就发送
        String sessionId = redisSessionService.get(username);
        if (Objects.nonNull(sessionId)) {
            messagingTemplate.convertAndSend("/topic/" + username, "hello:" + username);
        }
//        messagingTemplate.convertAndSendToUser(username, "/topic/greetings", "hello:"+username);
    }

    /**
     * 模拟登录
     *
     * @param request
     * @param name
     * @return
     */
    @RequestMapping(value = "loginIn", method = RequestMethod.GET)
    public String login(HttpServletRequest request, String name) {
        HttpSession httpSession = request.getSession();
        // 如果登录成功，则保存到会话中
        httpSession.setAttribute("loginName", name);
        return "login success";
    }
}
