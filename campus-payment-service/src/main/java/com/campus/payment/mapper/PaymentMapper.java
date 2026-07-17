package com.campus.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

    @Select("SELECT * FROM payment WHERE request_id = #{requestId} AND deleted = 0")
    Payment findByRequestId(@Param("requestId") String requestId);
}
