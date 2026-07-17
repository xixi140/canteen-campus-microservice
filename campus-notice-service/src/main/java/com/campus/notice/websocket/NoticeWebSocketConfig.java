package com.campus.notice.websocket;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置 —— 注册 @ServerEndpoint 到 Spring 容器
 *
 * 同时提供一个全局配置类用于热部署
 */
@Configuration
public class NoticeWebSocketConfig {

    /**
     * 自动注册 @ServerEndpoint 注解的 Bean
     * Spring Boot 4.x 使用 Jakarta WebSocket
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
