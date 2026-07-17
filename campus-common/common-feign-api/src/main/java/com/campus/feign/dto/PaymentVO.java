package com.campus.feign.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付VO - Feign传输对象
 */
@Data
public class PaymentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String status;   // PENDING / SUCCESS / FAILED / REFUNDED
    private String payTime;
}
