package com.campus.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体 — user_db.user 表
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;        // 用户名
    private String password;        // 密码（BCrypt加密）
    private String nickname;        // 昵称
    private String phone;           // 手机号
    private String avatar;          // 头像URL
    private String role;            // 角色：STUDENT/MERCHANT/ADMIN

    @TableLogic
    private Integer deleted;        // 逻辑删除：0-正常 1-删除

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
