package com.campus.payment.controller;

import com.campus.common.result.R;
import com.campus.feign.dto.PaymentVO;
import com.campus.payment.entity.Payment;
import com.campus.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付服务 Controller
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentMapper paymentMapper;

    /**
     * 创建支付单（Feign调用入口 —— 下单时自动调用）
     *
     * 幂等性：同一 requestId 只创建一次支付单
     */
    @PostMapping("/create")
    @Transactional
    public R<PaymentVO> create(@RequestParam Long orderId,
                               @RequestParam BigDecimal amount,
                               @RequestParam String requestId) {
        // 幂等性校验
        Payment existing = paymentMapper.findByRequestId(requestId);
        if (existing != null) {
            PaymentVO vo = new PaymentVO();
            vo.setId(existing.getId());
            vo.setOrderId(existing.getOrderId());
            vo.setAmount(existing.getAmount());
            vo.setStatus(existing.getStatus());
            return R.ok(vo);
        }

        // 创建支付单
        Payment payment = new Payment();
        payment.setRequestId(requestId);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");  // 模拟支付成功（实际接入微信/支付宝）
        payment.setPayMethod("WECHAT");
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setPayTime(LocalDateTime.now());
        paymentMapper.insert(payment);

        PaymentVO vo = new PaymentVO();
        vo.setId(payment.getId());
        vo.setOrderId(payment.getOrderId());
        vo.setAmount(payment.getAmount());
        vo.setStatus(payment.getStatus());
        vo.setPayTime(payment.getPayTime().toString());
        return R.ok(vo);
    }

    /**
     * 查询支付状态
     */
    @GetMapping("/{id}/status")
    public R<String> getStatus(@PathVariable Long id) {
        Payment payment = paymentMapper.selectById(id);
        if (payment == null) {
            return R.fail("支付单不存在");
        }
        return R.ok(payment.getStatus());
    }
}
