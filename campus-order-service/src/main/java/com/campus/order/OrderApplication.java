package com.campus.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务启动类 —— 微服务核心枢纽
 *
 * 职责：
 * 1. 统一下单（食堂菜品 + 商户菜品）
 * 2. 通过 OpenFeign 调用 user-service、canteen-service、merchant-service、payment-service
 * 3. 通过 Sentinel 实现熔断降级
 * 4. 通过唯一 requestId 实现幂等性
 * 5. 通过乐观锁实现库存并发控制
 *
 * 数据库：order_db
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.campus.feign", "com.campus.order.feign"})
@MapperScan("com.campus.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
