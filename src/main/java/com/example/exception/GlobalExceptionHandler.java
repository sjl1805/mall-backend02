package com.example.exception;

import com.example.common.Result;
import com.example.common.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.failed(e.getResultCode(), e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = getBindingResultErrorMessage(bindingResult);
        log.error("参数验证异常: {}", message);
        return Result.validateFailed(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = getBindingResultErrorMessage(e.getBindingResult());
        log.error("参数绑定异常: {}", message);
        return Result.validateFailed(message);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.error("约束违反异常: {}", message);
        return Result.validateFailed(message);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数: {}", e.getMessage());
        return Result.validateFailed("缺少必要的请求参数: " + e.getParameterName());
    }

    /**
     * 处理请求参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配: {}", e.getMessage());
        return Result.validateFailed("参数类型不匹配: " + e.getName());
    }

    /**
     * 处理HTTP请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持的HTTP方法: {}", e.getMessage());
        return Result.failed(ResultCode.FAILED, "不支持的HTTP方法: " + e.getMethod());
    }

    /**
     * 处理请求体无法读取异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求体无法读取: {}", e.getMessage());
        return Result.validateFailed("请求体无法读取，请检查请求内容格式是否正确");
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("资源未找到: {}", e.getMessage());
        return Result.failed(ResultCode.NOT_FOUND, "请求的资源不存在: " + e.getRequestURL());
    }

    /**
     * 处理元素不存在异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoSuchElementException(NoSuchElementException e) {
        log.error("查询的资源不存在: {}", e.getMessage());
        return Result.failed(ResultCode.NOT_FOUND, "查询的资源不存在");
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常: {}", e.getMessage());
        
        if (e instanceof BadCredentialsException) {
            return Result.failed(ResultCode.PASSWORD_ERROR);
        } else if (e instanceof LockedException) {
            return Result.failed(ResultCode.USER_LOCKED);
        } else if (e instanceof DisabledException) {
            return Result.failed(ResultCode.USER_INACTIVE);
        }
        
        return Result.unauthorized();
    }

    /**
     * 处理JWT相关异常
     */
    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, 
                      io.jsonwebtoken.security.SecurityException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleJwtException(Exception e) {
        log.error("JWT解析异常: {}", e.getMessage());
        if (e instanceof ExpiredJwtException) {
            return Result.failed(ResultCode.UNAUTHORIZED, "令牌已过期");
        }
        return Result.unauthorized();
    }

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传大小超限: {}", e.getMessage());
        return Result.failed(ResultCode.FILE_UPLOAD_ERROR, "上传文件过大");
    }

    /**
     * 处理数据库异常
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleSQLException(Exception e) {
        log.error("数据库异常: ", e);
        return Result.failed(ResultCode.DATABASE_ERROR);
    }

    /**
     * 处理Redis连接异常
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        log.error("Redis连接异常: ", e);
        return Result.failed(ResultCode.CACHE_ERROR);
    }

    /**
     * 处理超时异常
     */
    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public Result<Void> handleTimeoutException(TimeoutException e) {
        log.error("请求超时: ", e);
        return Result.failed(ResultCode.NETWORK_ERROR, "请求处理超时");
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.failed(ResultCode.SYSTEM_ERROR, "系统发生异常，请联系管理员");
    }

    /**
     * 从 BindingResult 中提取详细的错误信息
     */
    private String getBindingResultErrorMessage(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
} 