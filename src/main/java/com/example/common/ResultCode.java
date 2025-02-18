package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // 基础状态码
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数校验失败"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    
    // 业务状态码（1000-1999）
    USER_NOT_EXIST(1001, "User Not Exist", "用户不存在"),
    PRODUCT_OFF_SHELF(1002, "Product Off Shelf", "商品已下架"),
    STOCK_NOT_ENOUGH(1003, "Insufficient Stock", "库存不足"),
    ORDER_STATUS_ERROR(1004, "Order Status Error", "订单状态异常"),
    COUPON_EXPIRED(1005, "Coupon Expired", "优惠券已过期"),
    REPEAT_OPERATION(1006, "Repeat Operation", "重复操作"),
    USER_EXIST(1007, "User Exist", "用户已存在"),
    USER_NAME_OR_PASSWORD_ERROR(1008, "User Name Or Password Error", "用户名或密码错误"),
    
    // 新增用户相关错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "密码错误"),
    USERNAME_EXISTS(1003, "用户名已存在"),
    REGISTER_ERROR(1004, "注册失败"),
    
    // 系统级错误
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_ERROR(501, "业务逻辑错误"),
    REMOTE_ERROR(502, "远程调用失败"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "服务不可用"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "网关超时");

    private final Integer code;
    private final String message;
    private final String enMsg;  // 英文错误信息
    private final String zhMsg;  // 中文错误信息

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.enMsg = message;
        this.zhMsg = message;
    }

    ResultCode(Integer code, String enMsg, String zhMsg) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
        this.message = zhMsg;
    }

    public String getMessage(Language language) {
        return language == Language.EN ? enMsg : zhMsg;
    }

    public enum Language {
        EN, ZH
    }
} 