package com.campus.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 支付服务启动类
 *
 * 职责：支付创建、支付状态管理、幂等性保障
 * 数据库：payment_db
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.campus.payment.mapper")
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
