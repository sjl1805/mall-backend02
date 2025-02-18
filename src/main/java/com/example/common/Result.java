package com.example.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应结果")
public class Result<T> implements Serializable {
    @Schema(description = "状态码", example = "200")
    private Integer code;
    
    @Schema(description = "业务消息", example = "操作成功")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
    
    @Schema(description = "错误详情（开发环境可见）")
    private String debugInfo;
    
    @Schema(description = "时间戳", example = "1672531200000")
    private Long timestamp = System.currentTimeMillis();

    // 成功响应
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    // 失败响应
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 分页响应
    public static <T> Result<PageResult<T>> page(T data, long total, int page, int size) {
        PageResult<T> pageResult = new PageResult<>(data, total, page, size);
        return success(pageResult);
    }

    // 异常处理
    public static <T> Result<T> error(Exception e) {
        return error(e, true);
    }

    public static <T> Result<T> error(Exception e, boolean showDetail) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.INTERNAL_ERROR.getCode());
        result.setMessage(showDetail ? e.getMessage() : ResultCode.INTERNAL_ERROR.getMessage());
        result.setDebugInfo(showDetail ? e.getClass().getName() : null);
        return result;
    }

    // HTTP状态码转换
    public static <T> Result<T> fromHttpStatus(HttpStatus status) {
        return new Result<T>().code(status.value()).message(status.getReasonPhrase());
    }

    // 链式调用方法
    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> debugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
        return this;
    }

    // 分页结果内部类
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PageResult<T> {
        private T list;
        private long total;
        private int page;
        private int size;
        private int pages;

        public PageResult(T list, long total, int page, int size) {
            this.list = list;
            this.total = total;
            this.page = page;
            this.size = size;
            this.pages = (int) Math.ceil((double) total / size);
        }
    }
} 