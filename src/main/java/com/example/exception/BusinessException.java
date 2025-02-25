package com.example.common;

import com.example.common.IResultCode;

class BusinessException extends RuntimeException {
    private final IResultCode resultCode;

    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(IResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public IResultCode getResultCode() {
        return resultCode;
    }
} 