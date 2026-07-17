package com.campus.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 通知服务启动类
 *
 * 职责：订单状态推送通知（SSE/WebSocket）、系统公告
 * 数据库：notice_db
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NoticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }
}
