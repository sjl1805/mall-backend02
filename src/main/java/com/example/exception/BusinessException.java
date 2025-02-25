package com.example.exception;

import com.example.common.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 * 用于在业务逻辑中抛出特定错误码的异常
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final ResultCode resultCode;
    
    /**
     * 使用错误码和默认错误信息构造异常
     * 
     * @param resultCode 错误码
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
    
    /**
     * 使用错误码和自定义错误信息构造异常
     * 
     * @param resultCode 错误码
     * @param message 自定义错误信息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
    
    /**
     * 使用错误码、自定义错误信息和异常原因构造异常
     * 
     * @param resultCode 错误码
     * @param message 自定义错误信息
     * @param cause 异常原因
     */
    public BusinessException(ResultCode resultCode, String message, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }
    
    /**
     * 使用错误码和异常原因构造异常
     * 
     * @param resultCode 错误码
     * @param cause 异常原因
     */
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    public BusinessException(String message) {
        super(message);
        this.resultCode = ResultCode.INTERNAL_SERVER_ERROR;
    }
} 