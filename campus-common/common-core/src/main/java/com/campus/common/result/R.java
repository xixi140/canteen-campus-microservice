package com.campus.common.result;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应体 —— 所有API返回格式统一为 {code, message, data}
 *
 * 设计原则：
 * - 前端无需判断不同的返回格式
 * - 网关可以统一处理响应
 * - Feign调用方可以统一解析
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态码：200-成功，其他-失败 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 响应数据 */
    private T data;
    /** 时间戳（用于排查问题） */
    private long timestamp;

    private R() {
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功响应 ====================

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.code = 200;
        r.message = "操作成功";
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = ok();
        r.data = data;
        return r;
    }

    public static <T> R<T> ok(String message, T data) {
        R<T> r = ok(data);
        r.message = message;
        return r;
    }

    // ==================== 失败响应 ====================

    public static <T> R<T> fail() {
        R<T> r = new R<>();
        r.code = 500;
        r.message = "操作失败";
        return r;
    }

    public static <T> R<T> fail(String message) {
        R<T> r = fail();
        r.message = message;
        return r;
    }

    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<>();
        r.code = code;
        r.message = message;
        r.timestamp = System.currentTimeMillis();
        return r;
    }

    // ==================== 判断方法 ====================

    public boolean isSuccess() {
        return this.code == 200;
    }
}
