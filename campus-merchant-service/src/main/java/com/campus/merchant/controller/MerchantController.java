package com.campus.merchant.controller;

import com.campus.common.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商户/店铺 Controller
 *
 * 提供商户列表、店铺详情的 REST API
 */
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    /** 模拟店铺数据 (shopId -> shopInfo) */
    private static final Map<Long, Map<String, Object>> SHOP_MAP = new ConcurrentHashMap<>();

    static {
        Map<String, Object> s1 = new LinkedHashMap<>();
        s1.put("id", 1L);
        s1.put("name", "老王炒饭");
        s1.put("merchantId", 1001L);
        s1.put("merchantName", "王师傅");
        s1.put("category", "炒饭/盖饭");
        s1.put("phone", "13800001111");
        s1.put("description", "专注炒饭20年，深受学生喜爱");
        s1.put("rating", 4.5);
        s1.put("status", 1);
        SHOP_MAP.put(1L, s1);

        Map<String, Object> s2 = new LinkedHashMap<>();
        s2.put("id", 2L);
        s2.put("name", "张姐麻辣烫");
        s2.put("merchantId", 1002L);
        s2.put("merchantName", "张姐");
        s2.put("category", "麻辣烫/冒菜");
        s2.put("phone", "13800002222");
        s2.put("description", "正宗四川麻辣烫，麻辣鲜香");
        s2.put("rating", 4.3);
        s2.put("status", 1);
        SHOP_MAP.put(2L, s2);

        Map<String, Object> s3 = new LinkedHashMap<>();
        s3.put("id", 3L);
        s3.put("name", "小陈饮品");
        s3.put("merchantId", 1003L);
        s3.put("merchantName", "小陈");
        s3.put("category", "奶茶/果汁");
        s3.put("phone", "13800003333");
        s3.put("description", "新鲜水果现制，每日特价");
        s3.put("rating", 4.7);
        s3.put("status", 1);
        SHOP_MAP.put(3L, s3);

        Map<String, Object> s4 = new LinkedHashMap<>();
        s4.put("id", 4L);
        s4.put("name", "煎饼小屋");
        s4.put("merchantId", 1004L);
        s4.put("merchantName", "李阿姨");
        s4.put("category", "煎饼/早餐");
        s4.put("phone", "13800004444");
        s4.put("description", "杂粮煎饼、鸡蛋灌饼，早餐首选");
        s4.put("rating", 4.1);
        s4.put("status", 0);   // 已打烊
        SHOP_MAP.put(4L, s4);
    }

    /**
     * 获取所有商户/店铺列表
     */
    @GetMapping("/list")
    public R<List<Map<String, Object>>> listMerchants() {
        return R.ok(List.copyOf(SHOP_MAP.values()));
    }

    /**
     * 获取单个店铺详情
     */
    @GetMapping("/shop/{id}")
    public R<Map<String, Object>> getShop(@PathVariable Long id) {
        Map<String, Object> shop = SHOP_MAP.get(id);
        if (shop == null) {
            return R.fail("店铺不存在");
        }
        return R.ok(shop);
    }
}
