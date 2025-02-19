package com.example.exception;

import com.example.common.Result;
import com.example.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器 - 统一处理系统各类异常并返回标准格式响应
 * 
 * <p>本类使用@RestControllerAdvice注解实现以下功能：
 * 1. 统一异常响应格式
 * 2. 分类处理不同异常类型
 * 3. 记录异常日志
 * 4. 维护HTTP状态码与业务状态码的对应关系
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务逻辑异常
     * 
     * @param e 业务异常对象，包含预定义的错误码和消息
     * @return 标准错误响应，HTTP状态码200（业务异常不改变HTTP状态）
     * 
     * @apiNote 业务异常通常由开发者主动抛出，用于处理可预见的业务错误
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getResultCode());
    }

    /**
     * 处理参数校验异常
     * 
     * @param ex 参数校验异常，可能是MethodArgumentNotValidException或BindException
     * @return 参数错误响应，包含具体校验失败信息，HTTP状态码400
     * 
     * @apiNote 统一处理Spring Validation框架抛出的校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidationException(Exception ex) {
        String message = "参数校验失败";
        if (ex instanceof MethodArgumentNotValidException e) {
            message = e.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        } else if (ex instanceof BindException e) {
            message = e.getBindingResult().getFieldErrors().stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        }
        return Result.error(ResultCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理404资源未找到异常
     * 
     * @param e 未找到处理器异常
     * @return 资源未找到响应，HTTP状态码404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handle404(NoHandlerFoundException e) {
        return Result.error(ResultCode.NOT_FOUND.getCode(), 
                         "请求资源不存在: " + e.getRequestURL());
    }

    /**
     * 处理系统未捕获异常
     * 
     * @param req HTTP请求对象
     * @param e 未预期的异常对象
     * @return 系统错误响应，HTTP状态码500
     * 
     * @apiNote 生产环境应隐藏详细错误信息，开发环境可返回完整堆栈
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGlobalException(HttpServletRequest req, Exception e) {
        String errorMsg = String.format("系统异常: %s => %s", 
                                      req.getRequestURI(), 
                                      e.getMessage());
        // 此处应记录错误日志（实际项目需添加日志实现）
        System.out.println(errorMsg);
        return Result.error(ResultCode.INTERNAL_ERROR.getCode(), 
                          "系统繁忙，请稍后再试");
    }
} 