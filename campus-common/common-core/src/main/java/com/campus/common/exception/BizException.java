package com.campus.common.exception;

import lombok.Getter;

/**
 * 业务异常 —— 统一异常处理
 *
 * 使用场景：
 * - 参数校验失败
 * - 业务规则不满足
 * - 第三方服务调用失败
 *
 * 会被 GlobalExceptionHandler 统一捕获并返回 R.fail()
 */
@Getter
public class BizException extends RuntimeException {

    /** 错误码 */
    private final int code;

    public BizException(String message) {
        super(message);
        this.code = 500;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }
}
