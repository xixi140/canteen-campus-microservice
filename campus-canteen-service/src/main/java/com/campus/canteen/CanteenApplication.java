package com.campus.canteen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 食堂菜品服务启动类
 *
 * 职责：食堂管理、档口管理、官方菜品发布、分时段(早/午/晚)菜品查询
 * 数据库：canteen_db
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.campus.canteen.mapper")
public class CanteenApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanteenApplication.class, args);
    }
}
