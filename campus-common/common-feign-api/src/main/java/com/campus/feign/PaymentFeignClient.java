package com.campus.feign;

import com.campus.common.result.R;
import com.campus.feign.dto.PaymentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 支付服务 Feign 客户端接口
 */
@FeignClient(name = "payment-service", path = "/api/payment")
public interface PaymentFeignClient {

    /**
     * 创建支付单
     */
    @PostMapping("/create")
    R<PaymentVO> create(@RequestParam("orderId") Long orderId,
                        @RequestParam("amount") BigDecimal amount,
                        @RequestParam("requestId") String requestId);

    /**
     * 查询支付状态
     */
    @GetMapping("/{id}/status")
    R<String> getStatus(@PathVariable("id") Long id);
}
