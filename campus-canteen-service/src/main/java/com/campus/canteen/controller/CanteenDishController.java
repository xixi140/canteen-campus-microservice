package com.campus.canteen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.canteen.entity.CanteenDish;
import com.campus.canteen.mapper.CanteenDishMapper;
import com.campus.common.exception.BizException;
import com.campus.common.result.R;
import com.campus.feign.dto.CanteenDishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 食堂菜品 Controller
 *
 * 对外：提供REST API给学生端查看菜品
 * 对内：实现Feign接口契约供订单服务调用
 */
@RestController
@RequestMapping("/api/canteen")
@RequiredArgsConstructor
public class CanteenDishController {

    private final CanteenDishMapper dishMapper;

    // ========== 对外 REST API — 学生查看菜品 ==========

    /**
     * 查询某食堂某餐次的今日菜品
     */
    @GetMapping("/dishes")
    public R<List<CanteenDish>> listDishes(@RequestParam Long canteenId,
                                           @RequestParam(required = false) String mealType) {
        LambdaQueryWrapper<CanteenDish> wrapper = new LambdaQueryWrapper<CanteenDish>()
                .eq(CanteenDish::getStatus, 1);
        if (mealType != null) {
            wrapper.eq(CanteenDish::getMealType, mealType);
        }
        return R.ok(dishMapper.selectList(wrapper));
    }

    // ========== Feign 内部调用接口 — 供 order-service 调用 ==========

    /**
     * 根据ID查询菜品（Feign调用入口）
     */
    @GetMapping("/dishes/{id}")
    public R<CanteenDishVO> getDishById(@PathVariable Long id) {
        CanteenDish dish = dishMapper.selectById(id);
        if (dish == null) {
            return R.fail("食堂菜品不存在");
        }
        CanteenDishVO vo = new CanteenDishVO();
        vo.setId(dish.getId());
        vo.setName(dish.getName());
        vo.setPrice(dish.getPrice());
        vo.setStock(dish.getStock());
        vo.setMealType(dish.getMealType());
        vo.setImageUrl(dish.getImageUrl());
        vo.setStallId(dish.getStallId());
        return R.ok(vo);
    }

    /**
     * 扣减库存（Feign调用入口 —— 下单时调用）
     * 使用乐观锁防止超卖
     */
    @PutMapping("/dishes/{id}/stock/deduct")
    @Transactional
    public R<Void> deductStock(@PathVariable Long id,
                               @RequestParam Integer quantity) {
        CanteenDish dish = dishMapper.selectById(id);
        if (dish == null) {
            return R.fail("菜品不存在");
        }
        // 乐观锁扣减：如果版本号变了，返回0行
        int rows = dishMapper.deductStock(id, quantity, dish.getVersion());
        if (rows == 0) {
            throw new BizException("库存不足或系统繁忙，请重试");
        }
        return R.ok();
    }

    /**
     * 检查库存
     */
    @GetMapping("/dishes/{id}/stock/check")
    public R<Boolean> checkStock(@PathVariable Long id, @RequestParam Integer quantity) {
        CanteenDish dish = dishMapper.selectById(id);
        return R.ok(dish != null && dish.getStock() >= quantity);
    }
}
