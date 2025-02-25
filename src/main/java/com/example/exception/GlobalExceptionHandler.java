package com.example.common;

import com.example.common.CommonResult;
import com.example.common.ResultCode;
import com.example.exception.UsernameExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数验证异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<String> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed(ResultCode.VALIDATE_FAILED, message);
    }

    /**
     * 处理绑定异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public CommonResult<String> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed(ResultCode.VALIDATE_FAILED, message);
    }

    /**
     * 处理用户名已存在异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UsernameExistsException.class)
    public CommonResult<String> handleUsernameExistsException(UsernameExistsException e) {
        log.error("用户名已存在异常：{}", e.getMessage());
        return CommonResult.failed(ResultCode.USERNAME_EXISTED, e.getMessage());
    }

    /**
     * 处理访问拒绝异常
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("没有权限：{}", e.getMessage());
        return CommonResult.failed(ResultCode.FORBIDDEN, "没有权限执行此操作");
    }

    /**
     * 处理所有其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e) {
        log.error("系统异常：", e);
        return CommonResult.failed(ResultCode.FAILED, "系统异常，请联系管理员");
    }
}

