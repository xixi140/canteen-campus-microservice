package com.campus.notice.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知实体
 *
 * 用于存储系统通知、订单状态推送消息等
 */
@Data
public class Notice {

    private Long id;
    private Long userId;           // 接收通知的用户ID
    private String title;          // 通知标题
    private String content;        // 通知内容
    private String type;           // 通知类型：ORDER_STATUS / SYSTEM / PROMOTION
    private Boolean isRead;        // 是否已读
    private LocalDateTime createTime; // 创建时间
}
