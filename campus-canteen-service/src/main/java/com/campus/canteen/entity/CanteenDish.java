package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 食堂菜品实体 — canteen_db.canteen_dish 表
 *
 * 区别于商户菜品：
 * - 有餐次时段（早/午/晚餐）
 * - 有供应日期
 * - 价格由学校统一定价
 */
@Data
@TableName("canteen_dish")
public class CanteenDish {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stallId;            // 所属档口ID
    private String name;             // 菜品名称：红烧肉、番茄炒蛋
    private BigDecimal price;        // 价格（学校统一定价）
    private Integer stock;           // 当日库存（份数）
    private String mealType;         // 餐次：BREAKFAST/LUNCH/DINNER
    private LocalDate supplyDate;    // 供应日期（当天）
    private String imageUrl;         // 菜品图片
    private String description;      // 菜品描述
    private Integer status;          // 状态：1-供应中 0-已售罄

    @Version
    private Integer version;         // 乐观锁版本号（防止并发超卖）

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
