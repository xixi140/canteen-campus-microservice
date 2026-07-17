package com.campus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API 网关启动类
 *
 * 职责：
 * 1. 统一入口：所有前端请求通过网关转发到后端微服务
 * 2. 路由转发：根据请求路径动态路由到对应服务
 * 3. 负载均衡：与 Nacos 集成，自动负载均衡
 * 4. 限流熔断：集成 Sentinel 进行流量控制和熔断降级
 */
@SpringBootApplication
@EnableDiscoveryClient  // 注册到 Nacos
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
