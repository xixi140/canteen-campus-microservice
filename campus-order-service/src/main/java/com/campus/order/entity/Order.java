package com.campus.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体 — order_db.`order` 表
 */
@Data
@TableName("`order`")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 幂等键：唯一请求ID，防止重复下单（DB层有 UNIQUE INDEX） */
    private String requestId;

    private Long userId;             // 用户ID
    private String sourceType;       // 菜品来源：CANTEEN/MERCHANT/MIXED
    private Long dishId;             // 菜品ID
    private String dishName;         // 菜品名称（冗余，方便查询）
    private Integer quantity;        // 数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal totalAmount;  // 总金额
    private String status;           // 订单状态：PENDING_PAYMENT/PAID/PREPARING/COMPLETED/CANCELLED
    private Long paymentId;          // 关联支付单ID
    private String remark;           // 备注

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
