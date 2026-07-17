package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.CanteenDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CanteenDishMapper extends BaseMapper<CanteenDish> {

    /**
     * 扣减库存（乐观锁 —— 防止超卖）
     * WHERE version = oldVersion → 如果版本号变了，更新失败
     */
    @Update("UPDATE canteen_dish SET stock = stock - #{quantity}, " +
            "version = version + 1, status = CASE WHEN stock - #{quantity} <= 0 THEN 0 ELSE 1 END " +
            "WHERE id = #{id} AND stock >= #{quantity} AND version = #{version}")
    int deductStock(@Param("id") Long id,
                    @Param("quantity") Integer quantity,
                    @Param("version") Integer version);
}
