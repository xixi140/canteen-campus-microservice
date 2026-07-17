package com.campus.merchant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商户菜品服务启动类
 *
 * 职责：商户入驻、店铺管理、自定义菜品上架/下架
 * 数据库：merchant_db
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.campus.merchant.mapper")
public class MerchantApplication {
    public static void main(String[] args) {
        SpringApplication.run(MerchantApplication.class, args);
    }
}
