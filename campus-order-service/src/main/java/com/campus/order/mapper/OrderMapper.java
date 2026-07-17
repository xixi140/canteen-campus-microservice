package com.campus.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据幂等键查询订单 —— 幂等性校验
     */
    @Select("SELECT * FROM `order` WHERE request_id = #{requestId} AND deleted = 0")
    Order findByRequestId(@Param("requestId") String requestId);
}
