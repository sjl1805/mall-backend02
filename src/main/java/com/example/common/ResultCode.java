package com.example.common;

/**
 * 返回结果状态码
 */
public enum ResultCode {
    
    // 通用状态码: 200-599
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "数据冲突"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    
    // 用户相关: 1000-1999
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_LOGIN_FAILED(1002, "用户名或密码错误"),
    USER_ACCOUNT_FORBIDDEN(1003, "用户已被禁用"),
    USER_NOT_LOGIN(1004, "用户未登录"),
    USER_ACCOUNT_EXPIRED(1005, "账号已过期"),
    USER_PASSWORD_EXPIRED(1006, "密码已过期"),
    USER_ALREADY_EXIST(1007, "用户名已存在"),
    USER_EMAIL_ALREADY_EXIST(1008, "邮箱已被注册"),
    USER_PHONE_ALREADY_EXIST(1009, "手机号已被注册"),
    USER_ROLE_ERROR(1010, "用户角色异常"),
    USER_UPDATE_FAILED(1011, "用户信息更新失败"),
    USER_AVATAR_UPLOAD_FAILED(1012, "头像上传失败"),
    
    // 商品分类相关: 1100-1199
    CATEGORY_NOT_EXIST(1100, "商品分类不存在"),
    CATEGORY_HAS_CHILD(1101, "分类下存在子分类，无法删除"),
    CATEGORY_HAS_PRODUCT(1102, "分类下存在商品，无法删除"),
    CATEGORY_NAME_DUPLICATE(1103, "分类名称已存在"),
    CATEGORY_LEVEL_ERROR(1104, "分类层级错误"),
    
    // 商品相关: 2000-2999
    PRODUCT_NOT_EXIST(2001, "商品不存在"),
    PRODUCT_STOCK_ERROR(2002, "商品库存不足"),
    PRODUCT_OFF_SHELF(2003, "商品已下架"),
    PRODUCT_SKU_NOT_EXIST(2004, "商品规格不存在"),
    PRODUCT_PRICE_ERROR(2005, "商品价格异常"),
    PRODUCT_ALREADY_EXIST(2006, "商品已存在"),
    PRODUCT_CREATE_FAILED(2007, "商品创建失败"),
    PRODUCT_UPDATE_FAILED(2008, "商品更新失败"),
    PRODUCT_DELETE_FAILED(2009, "商品删除失败"),
    PRODUCT_SPEC_ERROR(2010, "商品规格异常"),
    PRODUCT_IMAGE_UPLOAD_FAILED(2011, "商品图片上传失败"),
    PRODUCT_SKU_STOCK_ERROR(2012, "SKU库存不足"),
    
    // 订单相关: 3000-3999
    ORDER_NOT_EXIST(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态异常"),
    ORDER_CREATE_FAILED(3003, "订单创建失败"),
    ORDER_PAY_FAILED(3004, "订单支付失败"),
    ORDER_CANCEL_FAILED(3005, "订单取消失败"),
    ORDER_ITEM_NOT_EXIST(3006, "订单项不存在"),
    ORDER_CONFIRM_RECEIPT_FAILED(3007, "确认收货失败"),
    ORDER_ALREADY_PAID(3008, "订单已支付，请勿重复操作"),
    ORDER_ALREADY_CANCELED(3009, "订单已取消"),
    ORDER_ALREADY_COMPLETED(3010, "订单已完成"),
    ORDER_PAYMENT_TIMEOUT(3011, "订单支付超时"),
    ORDER_REFUND_FAILED(3012, "订单退款失败"),
    ORDER_NOT_SUPPORT_REFUND(3013, "当前订单状态不支持退款"),
    ORDER_DELIVERY_FAILED(3014, "订单发货失败"),
    
    // 购物车相关: 4000-4999
    CART_EMPTY(4001, "购物车为空"),
    CART_ITEM_NOT_EXIST(4002, "购物车项不存在"),
    CART_ITEM_ADD_FAILED(4003, "添加购物车失败"),
    CART_ITEM_UPDATE_FAILED(4004, "更新购物车失败"),
    CART_ITEM_DELETE_FAILED(4005, "删除购物车项失败"),
    CART_ITEM_EXCEED_LIMIT(4006, "购物车项数量超出限制"),
    
    // 收藏相关: 4100-4199
    FAVORITE_FOLDER_NOT_EXIST(4100, "收藏夹不存在"),
    FAVORITE_FOLDER_CREATE_FAILED(4101, "创建收藏夹失败"),
    FAVORITE_FOLDER_DELETE_FAILED(4102, "删除收藏夹失败"),
    FAVORITE_FOLDER_UPDATE_FAILED(4103, "更新收藏夹失败"),
    FAVORITE_ITEM_NOT_EXIST(4104, "收藏项不存在"),
    FAVORITE_ITEM_ADD_FAILED(4105, "添加收藏失败"),
    FAVORITE_ITEM_DELETE_FAILED(4106, "取消收藏失败"),
    FAVORITE_ITEM_ALREADY_EXIST(4107, "已收藏该商品"),
    
    // 优惠券相关: 4200-4299
    COUPON_NOT_EXIST(4200, "优惠券不存在"),
    COUPON_EXPIRED(4201, "优惠券已过期"),
    COUPON_NOT_START(4202, "优惠券未生效"),
    COUPON_ALREADY_USED(4203, "优惠券已使用"),
    COUPON_NOT_AVAILABLE(4204, "不满足优惠券使用条件"),
    COUPON_RECEIVE_FAILED(4205, "领取优惠券失败"),
    COUPON_STOCK_EMPTY(4206, "优惠券库存不足"),
    
    // 地址相关: 4300-4399
    ADDRESS_NOT_EXIST(4300, "收货地址不存在"),
    ADDRESS_ADD_FAILED(4301, "添加收货地址失败"),
    ADDRESS_UPDATE_FAILED(4302, "更新收货地址失败"),
    ADDRESS_DELETE_FAILED(4303, "删除收货地址失败"),
    ADDRESS_EXCEED_LIMIT(4304, "收货地址数量超出限制"),
    
    // 评价相关: 4400-4499
    REVIEW_NOT_EXIST(4400, "评价不存在"),
    REVIEW_ADD_FAILED(4401, "添加评价失败"),
    REVIEW_UPDATE_FAILED(4402, "更新评价失败"),
    REVIEW_DELETE_FAILED(4403, "删除评价失败"),
    REVIEW_NOT_ALLOWED(4404, "不允许评价"),
    REVIEW_ALREADY_EXIST(4405, "已评价过该商品"),
    
    // 文件上传相关: 5000-5999
    FILE_UPLOAD_ERROR(5001, "文件上传失败"),
    FILE_MAX_SIZE_EXCEEDED(5002, "文件大小超过限制"),
    FILE_TYPE_ERROR(5003, "文件类型不支持"),
    FILE_NOT_EXIST(5004, "文件不存在"),
    FILE_DELETE_ERROR(5005, "文件删除失败"),
    FILE_DOWNLOAD_ERROR(5006, "文件下载失败"),
    
    // 推荐商品相关: 6000-6099
    RECOMMEND_FAILED(6000, "获取推荐商品失败"),
    RECOMMEND_CONFIG_ERROR(6001, "推荐配置错误"),
    
    // 支付相关: 7000-7099
    PAYMENT_FAILED(7000, "支付失败"),
    PAYMENT_TIMEOUT(7001, "支付超时"),
    PAYMENT_CALLBACK_ERROR(7002, "支付回调异常"),
    PAYMENT_METHOD_NOT_SUPPORTED(7003, "不支持的支付方式"),
    PAYMENT_AMOUNT_ERROR(7004, "支付金额错误"),
    
    // 系统相关: 9000-9999
    SYSTEM_ERROR(9000, "系统错误，请联系管理员"),
    SERVICE_UNAVAILABLE(9001, "服务不可用"),
    DATABASE_ERROR(9002, "数据库操作异常"),
    CACHE_ERROR(9003, "缓存操作异常"),
    REMOTE_SERVICE_ERROR(9004, "远程服务调用异常");
    
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 
