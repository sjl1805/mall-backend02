package com.example.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 成功返回结果
     * @param data 数据
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage("操作成功");
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    /**
     * 成功返回结果
     * @param message 成功消息
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }
    
    /**
     * 成功返回结果
     * @param data 数据
     * @param message 成功消息
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }
    
    /**
     * 失败返回结果
     * @param resultCode 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(ResultCode resultCode, String message) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
    
    /**
     * 失败返回结果
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(String message) {
        return failed(ResultCode.FAILED, message);
    }
    
    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
    
    /**
     * 参数验证失败返回结果
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return failed(ResultCode.VALIDATE_FAILED, message);
    }
    
    /**
     * 未授权返回结果
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 未授权结果
     */
    public static <T> Result<T> unauthorized(String message) {
        return failed(ResultCode.UNAUTHORIZED, message);
    }
    
    /**
     * 禁止访问返回结果
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 禁止访问结果
     */
    public static <T> Result<T> forbidden(String message) {
        return failed(ResultCode.FORBIDDEN, message);
    }
} 