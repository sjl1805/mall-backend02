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
    INVALID_PHONE_FORMAT(1002, "Invalid Phone Format", "手机号格式不正确"),
    PRODUCT_OFF_SHELF(1003, "Product Off Shelf", "商品已下架"),
    STOCK_NOT_ENOUGH(1004, "Insufficient Stock", "库存不足"),
    REPEAT_OPERATION(1006, "Repeat Operation", "重复操作"),
    USER_EXIST(1007, "User Exist", "用户已存在"),
    USER_DISABLED(1008, "User Disabled", "用户已禁用"),
    USER_NAME_OR_PASSWORD_ERROR(1009, "User Name Or Password Error", "用户名或密码错误"),

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
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "网关超时"),

    // 分类相关错误码
    CATEGORY_NOT_FOUND(2001, "分类不存在"),
    CATEGORY_NAME_EXISTS(2002, "分类名称已存在"),
    CATEGORY_PARENT_INVALID(2003, "父分类不存在"),
    CATEGORY_PARENT_DISABLED(2004, "父分类已禁用"),
    CATEGORY_LEVEL_LIMIT(2005, "最多支持三级分类"),
    CATEGORY_LEVEL_CHANGE(2006, "分类层级不可修改"),

    // 商品相关错误码
    PRODUCT_NOT_FOUND(3001, "商品不存在"),
    PRODUCT_NAME_EXISTS(3002, "商品名称已存在"),
    PRODUCT_CATEGORY_INVALID(3003, "商品分类无效"),
    PRODUCT_STOCK_INSUFFICIENT(3004, "库存不足"),
    PRODUCT_STATUS_ERROR(3005, "商品状态异常"),

    // SKU相关错误码
    SKU_NOT_FOUND(4001, "SKU不存在"),
    SKU_STOCK_INSUFFICIENT(4002, "SKU库存不足"),
    SKU_STATUS_ERROR(4003, "SKU状态异常"),
    SKU_SPEC_DUPLICATE(4004, "规格组合重复"),

    // 规格相关错误码
    SPEC_NAME_EXISTS(5001, "规格名称已存在"),
    SPEC_VALUE_INVALID(5002, "规格值格式错误"),
    SPEC_NOT_FOUND(5003, "规格不存在"),

    // 评价相关错误码
    REVIEW_ALREADY_EXISTS(6001, "已存在评价"),
    REVIEW_NOT_FOUND(6002, "评价不存在"),
    REVIEW_CONTENT_INVALID(6003, "评价内容不合法"),

    // 推荐相关错误码
    RECOMMEND_TIME_CONFLICT(7001, "推荐时间冲突"),
    RECOMMEND_PRODUCT_INVALID(7002, "推荐商品无效"),
    RECOMMEND_TYPE_ERROR(7003, "推荐类型错误"),

    // 收藏夹相关错误码
    FOLDER_NAME_EXISTS(8001, "收藏夹名称已存在"),
    FOLDER_NOT_FOUND(8002, "收藏夹不存在"),
    FOLDER_ACCESS_DENIED(8003, "无权限操作该收藏夹"),

    // 收藏相关错误码
    FAVORITE_ALREADY_EXISTS(9001, "已收藏该商品"),
    FAVORITE_NOT_FOUND(9002, "收藏记录不存在"),
    FAVORITE_ACCESS_DENIED(9003, "无权限操作该收藏"),

    // 购物车相关错误码
    CART_ITEM_EXISTS(10001, "商品已在购物车中"),
    CART_ITEM_NOT_FOUND(10002, "购物车商品不存在"),
    CART_ITEM_QUANTITY_INVALID(10003, "商品数量不合法"),

    // 优惠券相关错误码
    COUPON_NAME_EXISTS(11001, "优惠券名称已存在"),
    COUPON_INVALID(11002, "优惠券不可用"),
    COUPON_EXPIRED(11003, "优惠券已过期"),
    COUPON_CONDITION_NOT_MET(11004, "不满足使用条件"),
    COUPON_ALREADY_ACQUIRED(12001, "已领取该优惠券"),
    USER_COUPON_NOT_FOUND(12002, "用户优惠券不存在"),
    COUPON_USED(12003, "优惠券已使用"),

    // 地址相关错误码
    ADDRESS_ACCESS_DENIED(13001, "无权限操作该地址"),
    ADDRESS_NOT_FOUND(13002, "地址不存在"),
    ADDRESS_LIMIT_EXCEEDED(13003, "地址数量已达上限"),

    // 用户行为相关错误码
    BEHAVIOR_DUPLICATE(14001, "重复记录用户行为"),
    BEHAVIOR_INVALID(14002, "无效的用户行为类型"),

    // 订单相关错误码
    ORDER_NOT_FOUND(15001, "订单不存在"),
    ORDER_STATUS_ERROR(15002, "订单状态异常"),
    ORDER_ITEM_INVALID(15003, "订单项不合法"),

    // 新增错误码
    FILE_NOT_FOUND(404, "文件未找到");

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