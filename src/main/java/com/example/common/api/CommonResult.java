package com.example.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;
    private Long timestamp;
    private String path;

    public CommonResult() {
        this.timestamp = Instant.now().getEpochSecond();
    }

    // 成功响应（无数据）
    public static <T> CommonResult<T> success() {
        return success(null);
    }

    // 成功响应（带数据）
    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    // 失败响应（基础版）
    public static <T> CommonResult<T> failed(IResultCode resultCode) {
        return failed(resultCode, resultCode.getMessage());
    }

    // 失败响应（带自定义消息）
    public static <T> CommonResult<T> failed(IResultCode resultCode, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(resultCode.getCode());
        result.setMessage(message);
        return result;
    }

    // 参数校验失败
    public static <T> CommonResult<T> validateFailed(String message) {
        return failed(ResultCode.BAD_REQUEST, message);
    }

    // 链式调用支持
    public CommonResult<T> path(String path) {
        this.path = path;
        return this;
    }
}


