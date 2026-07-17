package com.campus.merchant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户菜品实体 — merchant_db.merchant_dish 表
 *
 * 区别于食堂菜品：
 * - 商户自主定价
 * - 商户自主上架/下架
 * - 无餐次限制，全时段可售
 */
@Data
@TableName("merchant_dish")
public class MerchantDish {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long shopId;             // 所属店铺ID
    private Long merchantId;         // 所属商户ID
    private String name;             // 菜品名称
    private BigDecimal price;        // 价格（商户自主定价）
    private Integer stock;           // 库存
    private String category;         // 分类
    private String imageUrl;         // 图片
    private String description;      // 描述
    private Integer status;          // 状态：1-上架 0-下架

    @Version
    private Integer version;         // 乐观锁版本号

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
