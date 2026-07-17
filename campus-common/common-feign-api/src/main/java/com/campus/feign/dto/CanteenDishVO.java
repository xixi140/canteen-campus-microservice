package com.campus.feign.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 食堂菜品VO - Feign传输对象
 */
@Data
public class CanteenDishVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;           // 菜品名称
    private BigDecimal price;      // 价格
    private Integer stock;         // 库存
    private String mealType;       // 餐次：BREAKFAST/LUNCH/DINNER
    private String imageUrl;       // 图片
    private Long stallId;          // 档口ID
    private String stallName;      // 档口名称
    private Long canteenId;        // 食堂ID
    private String canteenName;    // 食堂名称
}
