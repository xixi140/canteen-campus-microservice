package com.campus.feign.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商户菜品VO - Feign传输对象
 */
@Data
public class MerchantDishVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;           // 菜品名称
    private BigDecimal price;      // 价格
    private Integer stock;         // 库存
    private String category;       // 分类
    private String imageUrl;       // 图片
    private Long shopId;           // 店铺ID
    private String shopName;       // 店铺名称
    private Long merchantId;       // 商户ID
}
