package com.campus.canteen.controller;

import com.campus.common.result.R;
import com.campus.canteen.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/canteen")
@RequiredArgsConstructor
public class CanteenInnovationController {

    private final StringRedisTemplate redis;

    private static final String CONGEST_KEY = "canteen:congestion:";
    private static final long CONGEST_TTL = 30; // 30秒过期

    // ==================== 食堂拥挤度系统 ====================

    /**
     * 获取所有食堂拥挤度
     * 计算逻辑：基于每个食堂的待取餐订单数
     * 0-5单 → 空闲 😊
     * 6-20单 → 宽松 🙂
     * 21-50单 → 拥挤 😅
     * 50+单  → 爆满 😱
     */
    @GetMapping("/congestion")
    public R<List<Map<String, Object>>> getCongestion() {
        List<Map<String, Object>> result = new ArrayList<>();

        // 模拟食堂拥挤度数据（实际应从 order-service 聚合）
        String[][] canteens = {
            {"1", "一食堂", "校园东区"},
            {"2", "二食堂", "校园西区"}
        };

        for (String[] c : canteens) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c[0]);
            item.put("name", c[1]);
            item.put("location", c[2]);

            // 从 Redis 获取或生成模拟数据
            String key = CONGEST_KEY + c[0];
            String cached = redis.opsForValue().get(key);
            int pendingOrders;

            if (cached != null) {
                pendingOrders = Integer.parseInt(cached);
            } else {
                // 模拟数据：一食堂人多，二食堂人少
                pendingOrders = "1".equals(c[0]) ? 35 + new Random().nextInt(20) : 8 + new Random().nextInt(10);
                redis.opsForValue().set(key, String.valueOf(pendingOrders), CONGEST_TTL, TimeUnit.SECONDS);
            }

            String level;
            String emoji;
            int waitMinutes;

            if (pendingOrders <= 5) {
                level = "空闲"; emoji = "😊"; waitMinutes = 2;
            } else if (pendingOrders <= 20) {
                level = "宽松"; emoji = "🙂"; waitMinutes = 5;
            } else if (pendingOrders <= 50) {
                level = "拥挤"; emoji = "😅"; waitMinutes = 12;
            } else {
                level = "爆满"; emoji = "😱"; waitMinutes = 25;
            }

            item.put("pendingOrders", pendingOrders);
            item.put("level", level);
            item.put("emoji", emoji);
            item.put("waitMinutes", waitMinutes);
            result.add(item);
        }
        return R.ok(result);
    }

    /**
     * 模拟：下单时更新拥挤度（由 order-service Feign 调用）
     */
    @PostMapping("/congestion/update")
    public R<Void> updateCongestion(@RequestParam Long canteenId, @RequestParam int delta) {
        String key = CONGEST_KEY + canteenId;
        redis.opsForValue().increment(key, delta);
        redis.expire(key, CONGEST_TTL, TimeUnit.SECONDS);
        return R.ok();
    }

    // ==================== 菜品营养信息 ====================

    /**
     * 获取菜品营养信息
     */
    @GetMapping("/dishes/{id}/nutrition")
    public R<Map<String, Object>> getNutrition(@PathVariable Long id) {
        // 菜品营养模拟数据
        Map<Long, Map<String, Object>> nutritionMap = new HashMap<>();

        nutritionMap.put(101L, Map.of("calories", 450, "protein", 28, "carbs", 12, "fat", 32, "tags", List.of("高蛋白")));
        nutritionMap.put(102L, Map.of("calories", 180, "protein", 10, "carbs", 15, "fat", 8, "tags", List.of("低卡", "减脂")));
        nutritionMap.put(103L, Map.of("calories", 280, "protein", 18, "carbs", 20, "fat", 14, "tags", List.of("高蛋白")));
        nutritionMap.put(401L, Map.of("calories", 380, "protein", 25, "carbs", 18, "fat", 22, "tags", List.of("高蛋白")));
        nutritionMap.put(601L, Map.of("calories", 320, "protein", 8, "carbs", 35, "fat", 16, "tags", List.of("碳水")));
        nutritionMap.put(11L, Map.of("calories", 420, "protein", 12, "carbs", 55, "fat", 15, "tags", List.of("碳水")));
        nutritionMap.put(12L, Map.of("calories", 480, "protein", 14, "carbs", 60, "fat", 18, "tags", List.of("碳水")));

        Map<String, Object> result = nutritionMap.getOrDefault(id,
                Map.of("calories", 300, "protein", 15, "carbs", 30, "fat", 15, "tags", List.of()));

        return R.ok(result);
    }

    // ==================== 智能推荐 ====================

    /**
     * 获取今日推荐菜品（基于销量+评分）
     */
    @GetMapping("/recommend")
    public R<List<Map<String, Object>>> getRecommend(@RequestParam(required = false) String mealType) {
        List<Map<String, Object>> recommends = new ArrayList<>();

        Object[][] dishes = {
            {101, "红烧肉", "🥩", 8.00, "CANTEEN"},
            {11,  "蛋炒饭", "🍚", 10.00, "MERCHANT"},
            {401, "宫保鸡丁", "🍗", 10.00, "CANTEEN"},
            {13,  "牛肉炒饭", "🥩", 18.00, "MERCHANT"},
            {501, "过桥米线", "🍜", 15.00, "CANTEEN"},
        };

        for (Object[] d : dishes) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", d[0]);
            item.put("name", d[1]);
            item.put("emoji", d[2]);
            item.put("price", d[3]);
            item.put("sourceType", d[4]);
            recommends.add(item);
        }

        return R.ok(recommends);
    }

    // ==================== 今日摄入统计（营养聚合） ====================

    /**
     * 批量查询菜品营养（按订单聚合时调用）
     */
    @PostMapping("/nutrition/batch")
    public R<Map<String, Object>> batchNutrition(@RequestBody List<Long> dishIds) {
        int totalCal = 0, totalProtein = 0, totalCarbs = 0, totalFat = 0;

        for (Long id : dishIds) {
            // 模拟营养数据
            int cal = 300 + (int)(Math.random() * 200);
            int pro = 10 + (int)(Math.random() * 20);
            int carb = 20 + (int)(Math.random() * 40);
            int fat = 10 + (int)(Math.random() * 20);
            totalCal += cal;
            totalProtein += pro;
            totalCarbs += carb;
            totalFat += fat;
        }

        return R.ok(Map.of(
            "totalCalories", totalCal,
            "totalProtein", totalProtein,
            "totalCarbs", totalCarbs,
            "totalFat", totalFat
        ));
    }
}
