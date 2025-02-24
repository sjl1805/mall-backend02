package com.example.mall.common.api;


// 枚举实现
public enum ResultCode implements IResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数检验失败"),
    UNAUTHORIZED(401, "未授权或token已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    
    // 业务相关状态码（6位数字，前两位表示模块）
    USER_NOT_EXIST(100001, "用户不存在"),
    PRODUCT_OFF_SHELF(200001, "商品已下架");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 
