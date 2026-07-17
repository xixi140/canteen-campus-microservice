package com.campus.notice.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 通知推送终端
 *
 * 前端连接：ws://localhost:8086/ws/notice/{userId}
 * 服务端推送：order-service 通过 HTTP 调用本服务接口触发
 */
@Slf4j
@Component
@ServerEndpoint("/ws/notice/{userId}")
public class NoticeWebSocket {

    /** 在线用户连接池 */
    private static final Map<Long, Session> SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        SESSIONS.put(userId, session);
        log.info("WebSocket 连接: userId={}, 当前在线: {}", userId, SESSIONS.size());
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") Long userId) {
        SESSIONS.remove(userId);
        log.info("WebSocket 断开: userId={}, 当前在线: {}", userId, SESSIONS.size());
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("userId") Long userId) {
        SESSIONS.remove(userId);
        log.error("WebSocket 错误: userId={}", userId, error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 客户端消息不处理
    }

    /**
     * 向指定用户推送消息
     */
    public static void sendToUser(Long userId, String message) {
        Session session = SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                log.debug("推送消息成功: userId={}", userId);
            } catch (IOException e) {
                log.error("推送消息失败: userId={}", userId, e);
                SESSIONS.remove(userId);
            }
        }
    }

    /** 获取在线用户数 */
    public static int getOnlineCount() {
        return SESSIONS.size();
    }
}
