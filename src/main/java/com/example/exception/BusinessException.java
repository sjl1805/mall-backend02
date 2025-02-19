package com.example.exception;

import com.example.common.ResultCode;

/**
 * 业务逻辑异常 - 用于处理明确的业务规则违反情况
 * 
 * <p>本异常应抛出在以下场景：
 * 1. 违反业务规则（如重复注册）
 * 2. 数据状态冲突（如订单已支付）
 * 3. 权限不足等安全限制
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 业务状态码（参见ResultCode枚举）
     */
    private final ResultCode resultCode;

    /**
     * 构造业务异常
     * 
     * @param resultCode 预定义的业务状态码
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * 获取业务状态码
     */
    public ResultCode getResultCode() {
        return resultCode;
    }
} 