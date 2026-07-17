package com.campus.common.constant;

/**
 * 系统常量
 */
public interface SystemConstants {

    /** JWT Token 请求头 */
    String TOKEN_HEADER = "Authorization";

    /** JWT Token 前缀 */
    String TOKEN_PREFIX = "Bearer ";

    /** 菜品来源：食堂 */
    String SOURCE_CANTEEN = "CANTEEN";

    /** 菜品来源：商户 */
    String SOURCE_MERCHANT = "MERCHANT";

    /** 订单状态 */
    interface OrderStatus {
        String PENDING_PAYMENT = "PENDING_PAYMENT";  // 待支付
        String PAID = "PAID";                         // 已支付
        String PREPARING = "PREPARING";               // 备餐中
        String COMPLETED = "COMPLETED";               // 已完成
        String CANCELLED = "CANCELLED";               // 已取消
    }

    /** 餐次类型 */
    interface MealType {
        String BREAKFAST = "BREAKFAST";  // 早餐
        String LUNCH = "LUNCH";          // 午餐
        String DINNER = "DINNER";        // 晚餐
    }
}
