package com.campus.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付实体 — payment_db.payment 表
 */
@Data
@TableName("payment")
public class Payment {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 幂等键：与订单的 requestId 对应（DB层有 UNIQUE INDEX） */
    private String requestId;

    private Long orderId;            // 关联订单ID
    private Long userId;             // 用户ID
    private BigDecimal amount;       // 支付金额
    private String status;           // 状态：PENDING/SUCCESS/FAILED/REFUNDED
    private String payMethod;        // 支付方式：WECHAT/ALIPAY/CAMPUS_CARD
    private String transactionId;    // 第三方交易号
    private LocalDateTime payTime;   // 支付时间

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
