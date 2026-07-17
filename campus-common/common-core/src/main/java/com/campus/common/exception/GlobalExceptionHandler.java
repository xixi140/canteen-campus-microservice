package com.campus.common.exception;

import com.campus.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * 统一捕获所有Controller抛出的异常，返回统一格式的 R 响应
 * 这样前端统一处理，无需关心具体异常类型
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public R<Void> handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    /**
     * 未知异常（兜底处理）
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleUnknownException(Exception e) {
        log.error("系统异常: ", e);
        return R.fail(500, "系统内部错误，请联系管理员");
    }
}
