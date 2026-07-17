package com.campus.feign.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户VO - Feign传输对象
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String role;  // STUDENT / MERCHANT / ADMIN
}
