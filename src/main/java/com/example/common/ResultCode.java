package com.example.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    // 基础状态码
    SUCCESS(200, "Success", "操作成功"),
    BAD_REQUEST(400, "Bad Request", "请求参数错误"),
    UNAUTHORIZED(401, "Unauthorized", "未授权访问"),
    FORBIDDEN(403, "Forbidden", "禁止访问"),
    NOT_FOUND(404, "Not Found", "资源不存在"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "请求方法不允许"),
    
    // 业务状态码（1000-1999）
    USER_NOT_EXIST(1001, "User Not Exist", "用户不存在"),
    PRODUCT_OFF_SHELF(1002, "Product Off Shelf", "商品已下架"),
    STOCK_NOT_ENOUGH(1003, "Insufficient Stock", "库存不足"),
    ORDER_STATUS_ERROR(1004, "Order Status Error", "订单状态异常"),
    COUPON_EXPIRED(1005, "Coupon Expired", "优惠券已过期"),
    REPEAT_OPERATION(1006, "Repeat Operation", "重复操作"),
    
    // 系统级错误
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "系统异常"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "服务不可用"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "网关超时");

    private final int code;
    private final String enMsg;  // 英文错误信息
    private final String zhMsg;  // 中文错误信息

    ResultCode(int code, String enMsg, String zhMsg) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
    }

    public String getMessage(Language language) {
        return language == Language.EN ? enMsg : zhMsg;
    }

    public enum Language {
        EN, ZH
    }
} 