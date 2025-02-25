package com.example.common;

/**
 * 响应状态码枚举
 */
public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "暂未登录或登录已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    // 用户相关错误码
    USER_ALREADY_EXISTS(1001, "用户名已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_LOCKED(1004, "用户已被锁定"),
    VERIFY_CODE_ERROR(1005, "验证码错误"),
    USER_REGISTER_FAILED(1006, "用户注册失败"),
    USER_UPDATE_FAILED(1007, "用户信息更新失败"),
    USER_DELETE_FAILED(1008, "用户删除失败"),
    USER_INACTIVE(1009, "用户未激活"),
    ACCOUNT_FREEZE(1010, "账号已被冻结"),
    
    // 商品相关错误码
    PRODUCT_NOT_FOUND(2001, "商品不存在"),
    PRODUCT_STOCK_ERROR(2002, "商品库存不足"),
    PRODUCT_OFF_SHELF(2003, "商品已下架"),
    PRODUCT_CATEGORY_ERROR(2004, "商品分类错误"),
    PRODUCT_SKU_NOT_FOUND(2005, "商品规格不存在"),
    PRODUCT_CREATE_FAILED(2006, "商品创建失败"),
    PRODUCT_UPDATE_FAILED(2007, "商品更新失败"),
    PRODUCT_DELETE_FAILED(2008, "商品删除失败"),
    PRODUCT_IMAGE_UPLOAD_FAILED(2009, "商品图片上传失败"),
    PRODUCT_PRICE_ERROR(2010, "商品价格错误"),
    
    // 订单相关错误码
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态错误"),
    ORDER_CREATE_FAILED(3003, "创建订单失败"),
    ORDER_PAY_FAILED(3004, "订单支付失败"),
    ORDER_CANCEL_FAILED(3005, "订单取消失败"),
    ORDER_ITEM_ERROR(3006, "订单商品信息错误"),
    ORDER_PRICE_ERROR(3007, "订单价格计算错误"),
    ORDER_UPDATE_FAILED(3008, "订单更新失败"),
    ORDER_CONFIRM_RECEIPT_FAILED(3009, "确认收货失败"),
    ORDER_EXPIRED(3010, "订单已过期"),
    
    // 购物车相关错误码
    CART_EMPTY(4001, "购物车为空"),
    CART_ITEM_NOT_FOUND(4002, "购物车商品不存在"),
    CART_ADD_FAILED(4003, "添加购物车失败"),
    CART_UPDATE_FAILED(4004, "更新购物车失败"),
    CART_DELETE_FAILED(4005, "删除购物车项失败"),
    CART_SELECT_ALL_FAILED(4006, "全选购物车失败"),
    CART_UNSELECT_ALL_FAILED(4007, "取消全选购物车失败"),
    CART_CLEAR_FAILED(4008, "清空购物车失败"),
    CART_ITEM_LIMIT_EXCEEDED(4009, "购物车商品数量超出限制"),
    
    // 地址相关错误码
    ADDRESS_NOT_FOUND(5001, "收货地址不存在"),
    ADDRESS_ADD_FAILED(5002, "添加收货地址失败"),
    ADDRESS_UPDATE_FAILED(5003, "更新收货地址失败"),
    ADDRESS_DELETE_FAILED(5004, "删除收货地址失败"),
    ADDRESS_SET_DEFAULT_FAILED(5005, "设置默认地址失败"),
    ADDRESS_LIMIT_EXCEEDED(5006, "收货地址数量超出限制"),
    ADDRESS_FORMAT_ERROR(5007, "地址格式错误"),
    
    // 优惠券相关错误码
    COUPON_NOT_FOUND(6001, "优惠券不存在"),
    COUPON_EXPIRED(6002, "优惠券已过期"),
    COUPON_USED(6003, "优惠券已使用"),
    COUPON_CONDITION_ERROR(6004, "不满足优惠券使用条件"),
    COUPON_RECEIVE_LIMIT(6005, "优惠券领取已达上限"),
    COUPON_ISSUE_FAILED(6006, "优惠券发放失败"),
    COUPON_RECEIVE_FAILED(6007, "优惠券领取失败"),
    COUPON_USE_FAILED(6008, "优惠券使用失败"),
    COUPON_NOT_STARTED(6009, "优惠券活动未开始"),
    COUPON_STOCK_EMPTY(6010, "优惠券库存不足"),
    
    // 支付相关错误码
    PAYMENT_FAILED(7001, "支付失败"),
    PAYMENT_TIMEOUT(7002, "支付超时"),
    REFUND_FAILED(7003, "退款失败"),
    PAYMENT_METHOD_NOT_SUPPORTED(7004, "不支持的支付方式"),
    PAYMENT_AMOUNT_ERROR(7005, "支付金额错误"),
    PAYMENT_ALREADY_PROCESSED(7006, "支付已处理"),
    REFUND_AMOUNT_EXCEEDED(7007, "退款金额超出限制"),
    REFUND_EXPIRED(7008, "退款已过期"),
    PAYMENT_SIGNATURE_ERROR(7009, "支付签名错误"),
    PAYMENT_CALLBACK_ERROR(7010, "支付回调处理错误"),
    
    // 推荐相关错误码
    RECOMMENDATION_FAILED(8001, "获取推荐失败"),
    RECOMMENDATION_EMPTY(8002, "暂无推荐内容"),
    RECOMMENDATION_ALGORITHM_ERROR(8003, "推荐算法错误"),
    RECOMMENDATION_DATA_ERROR(8004, "推荐数据错误"),
    RECOMMENDATION_USER_PROFILE_INCOMPLETE(8005, "用户画像不完整"),
    RECOMMENDATION_CATEGORY_NOT_FOUND(8006, "推荐分类不存在"),
    
    // 评论和评分相关错误码
    REVIEW_NOT_FOUND(8101, "评论不存在"),
    REVIEW_ADD_FAILED(8102, "添加评论失败"),
    REVIEW_UPDATE_FAILED(8103, "更新评论失败"),
    REVIEW_DELETE_FAILED(8104, "删除评论失败"),
    RATING_INVALID(8105, "无效的评分"),
    REVIEW_NOT_ALLOWED(8106, "不允许评论"),
    REVIEW_CONTENT_ILLEGAL(8107, "评论内容不合法"),
    
    // 物流和配送相关错误码
    LOGISTICS_NOT_FOUND(8201, "物流信息不存在"),
    LOGISTICS_UPDATE_FAILED(8202, "更新物流信息失败"),
    DELIVERY_AREA_NOT_SUPPORTED(8203, "不支持的配送区域"),
    DELIVERY_TIME_NOT_AVAILABLE(8204, "配送时间不可用"),
    LOGISTICS_TRACKING_FAILED(8205, "物流跟踪失败"),
    DELIVERY_FEE_CALCULATION_ERROR(8206, "配送费计算错误"),
    
    // 售后服务相关错误码
    AFTER_SALE_NOT_FOUND(8301, "售后单不存在"),
    AFTER_SALE_CREATE_FAILED(8302, "创建售后单失败"),
    AFTER_SALE_UPDATE_FAILED(8303, "更新售后单失败"),
    AFTER_SALE_CANCEL_FAILED(8304, "取消售后单失败"),
    AFTER_SALE_STATUS_ERROR(8305, "售后单状态错误"),
    AFTER_SALE_EXPIRED(8306, "售后已过期"),
    AFTER_SALE_ITEM_ERROR(8307, "售后商品信息错误"),
    
    // 搜索相关错误码
    SEARCH_FAILED(8401, "搜索失败"),
    SEARCH_RESULT_EMPTY(8402, "搜索结果为空"),
    SEARCH_PARAM_ERROR(8403, "搜索参数错误"),
    SEARCH_INDEX_ERROR(8404, "搜索索引错误"),
    
    // 系统错误码
    SYSTEM_ERROR(9001, "系统错误"),
    DATA_ERROR(9002, "数据异常"),
    FILE_UPLOAD_ERROR(9003, "文件上传失败"),
    NETWORK_ERROR(9004, "网络异常"),
    DATABASE_ERROR(9005, "数据库错误"),
    CACHE_ERROR(9006, "缓存错误"),
    CONFIG_ERROR(9007, "配置错误"),
    API_LIMIT_EXCEEDED(9008, "API调用次数超限"),
    THIRD_PARTY_SERVICE_ERROR(9009, "第三方服务错误"),
    OPERATION_TOO_FREQUENT(9010, "操作过于频繁，请稍后再试");
    
    private final int code;
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 