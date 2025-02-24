package com.example.mall.common.exception;

import com.example.mall.common.api.CommonResult;
import com.example.mall.common.api.IResultCode;
import com.example.mall.common.api.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public CommonResult<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: {}", e.getMessage(), e);
        return CommonResult.failed(e.getResultCode())
                .path(request.getRequestURI());
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        log.warn("参数校验异常: {}", message);
        return CommonResult.validateFailed(message)
                .path(request.getRequestURI());
    }

    // 处理JSR303校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .findFirst()
                .orElse("参数校验失败");
        log.warn("参数校验异常: {}", message);
        return CommonResult.validateFailed(message)
                .path(request.getRequestURI());
    }

    // 处理认证异常
    @ExceptionHandler({BadCredentialsException.class})
    public CommonResult<?> handleAuthenticationException(Exception e, HttpServletRequest request) {
        log.warn("认证失败: {}", e.getMessage());
        return CommonResult.failed(ResultCode.UNAUTHORIZED, "用户名或密码错误")
                .path(request.getRequestURI());
    }

    // 处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("权限不足: {}", e.getMessage());
        return CommonResult.failed(ResultCode.FORBIDDEN)
                .path(request.getRequestURI());
    }

    // 处理JSON解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("请求体解析失败: {}", e.getMessage());
        return CommonResult.validateFailed("请求体格式错误")
                .path(request.getRequestURI());
    }

    // 处理其他未捕获异常
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleGlobalException(Exception e, HttpServletRequest request) {
        log.error("系统异常: ", e);
        return CommonResult.failed(ResultCode.INTERNAL_SERVER_ERROR)
                .path(request.getRequestURI());
    }
}

// 正确分离自定义异常类（原 BusinessException.java 中的核心内容）
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