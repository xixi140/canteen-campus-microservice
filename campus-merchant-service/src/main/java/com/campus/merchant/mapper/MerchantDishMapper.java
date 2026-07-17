package com.campus.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.merchant.entity.MerchantDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MerchantDishMapper extends BaseMapper<MerchantDish> {

    /**
     * 扣减库存（乐观锁）
     */
    @Update("UPDATE merchant_dish SET stock = stock - #{quantity}, " +
            "version = version + 1, status = CASE WHEN stock - #{quantity} <= 0 THEN 0 ELSE 1 END " +
            "WHERE id = #{id} AND stock >= #{quantity} AND version = #{version}")
    int deductStock(@Param("id") Long id,
                    @Param("quantity") Integer quantity,
                    @Param("version") Integer version);
}
