package fast.cloud.nacos.websocket.config;

import fast.cloud.nacos.common.model.exception.CustomException;
import fast.cloud.nacos.common.model.utils.GsonUtil;
import fast.cloud.nacos.websocket.request.TaskEventDTO;
import fast.cloud.nacos.websocket.request.WebSocketReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static fast.cloud.nacos.websocket.constants.ConstantEnum.WEB_SOCKET_PARAM_ERROR;

/**
 * @author qinfuxiang
 */
@ServerEndpoint(value = "/web-socket/socket")
@Component
@Slf4j
public class WebSocketServer {

    @PostConstruct
    public void init() {
        log.info("web-socket success 加载");
    }

    private final AtomicInteger OnlineCount = new AtomicInteger(0);

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private final static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<>();

    /**
     * 标记每个用户 key：tenant-assetId , value: session
     */
    private final static Map<String, Session> clientMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("web-socket onOpen session:{}", session);
        SessionSet.add(session);
        // 在线数加1
        int cnt = OnlineCount.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
        this.sendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        Collection<Session> values = clientMap.values();
        log.info("before close clientMap.values:{}", values);
        log.info("to be removed session:{}", session);
        for (; ; ) {
            if (!values.remove(session)) {
                break;
            }
        }
        log.info("after close clientMap.values:{}", values);
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("web-socket param message :{}", message);
            WebSocketReq websocketReq = GsonUtil.toBean(message, WebSocketReq.class);
            String userId = websocketReq.getUserId();
            for (Long stationId : websocketReq.getStationIds()) {
                log.info("clientMap put start");
                // 这个put相当于订阅操作
                clientMap.put(this.assembleSessionKey(userId, stationId), session);
            }
            log.info("clientMap values:{}", clientMap);
        } catch (Exception e) {
            log.error("web-socket error:{}", e);
            throw new CustomException(WEB_SOCKET_PARAM_ERROR);
        }
    }

    /**
     * 出现错误
     */
    @OnError
    public void onError(Session session, Throwable e) {
        log.error("发生错误：{}，Session ID： {}", e.getMessage(), session.getId());
        log.error(e.getMessage(), e);
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     */
    private void sendMessage(Session session, String message) {
        try {
            //无用户连接
            if (ObjectUtils.isEmpty(session)) {
                return;
            }
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     */
    public void sendMessage(String userId, TaskEventDTO eventDTO) {
        try {
            Long stationId = eventDTO.getStationId();
            String sessionKey = this.assembleSessionKey(userId, stationId);

            log.info("rule checker web-socket mqMessage sessionKey:{}", sessionKey);
            Session session = clientMap.get(sessionKey);
            log.info("rule checker web-socket mqMessage session:{}", session);
            //无用户连接
            if (ObjectUtils.isEmpty(session)) {
                return;
            }

            session.getBasicRemote().sendText(GsonUtil.toJson(eventDTO));
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
        }
    }

    /**
     * 群发消息
     * <br/>心跳，防止nginx中断链接
     */
    void broadCastHeartbeat() {
        for (Session session : SessionSet) {
            if (session.isOpen()) {
                this.sendMessage(session, "pong");
            }
        }
    }

    /**
     * 拼装session key
     */
    private String assembleSessionKey(String userId, Long stationId) {
        return userId + "-" + stationId;
    }

}
