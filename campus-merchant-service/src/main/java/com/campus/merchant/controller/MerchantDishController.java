package com.campus.merchant.controller;

import com.campus.common.exception.BizException;
import com.campus.common.result.R;
import com.campus.feign.dto.MerchantDishVO;
import com.campus.merchant.entity.MerchantDish;
import com.campus.merchant.mapper.MerchantDishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户菜品 Controller
 */
@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantDishController {

    private final MerchantDishMapper dishMapper;

    // ========== 对外 REST API ==========

    @GetMapping("/dishes")
    public R<List<MerchantDish>> listDishes(@RequestParam(required = false) Long shopId) {
        if (shopId != null) {
            return R.ok(dishMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MerchantDish>()
                            .eq(MerchantDish::getShopId, shopId)
                            .eq(MerchantDish::getStatus, 1)));
        }
        return R.ok(dishMapper.selectList(null));
    }

    // ========== Feign 内部调用接口 ==========

    @GetMapping("/dishes/{id}")
    public R<MerchantDishVO> getDishById(@PathVariable Long id) {
        MerchantDish dish = dishMapper.selectById(id);
        if (dish == null) {
            return R.fail("商户菜品不存在");
        }
        MerchantDishVO vo = new MerchantDishVO();
        vo.setId(dish.getId());
        vo.setName(dish.getName());
        vo.setPrice(dish.getPrice());
        vo.setStock(dish.getStock());
        vo.setCategory(dish.getCategory());
        vo.setImageUrl(dish.getImageUrl());
        vo.setShopId(dish.getShopId());
        vo.setMerchantId(dish.getMerchantId());
        return R.ok(vo);
    }

    @PutMapping("/dishes/{id}/stock/deduct")
    @Transactional
    public R<Void> deductStock(@PathVariable Long id, @RequestParam Integer quantity) {
        MerchantDish dish = dishMapper.selectById(id);
        if (dish == null) {
            return R.fail("菜品不存在");
        }
        int rows = dishMapper.deductStock(id, quantity, dish.getVersion());
        if (rows == 0) {
            throw new BizException("库存不足或系统繁忙，请重试");
        }
        return R.ok();
    }

    @GetMapping("/dishes/{id}/stock/check")
    public R<Boolean> checkStock(@PathVariable Long id, @RequestParam Integer quantity) {
        MerchantDish dish = dishMapper.selectById(id);
        return R.ok(dish != null && dish.getStock() >= quantity);
    }
}
