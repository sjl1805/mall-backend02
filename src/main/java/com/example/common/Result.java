package com.example.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 
 * <p>本类用于规范API响应格式，包含以下特性：
 * 1. 标准化响应结构（状态码、消息、数据、时间戳）
 * 2. 支持分页响应
 * 3. 异常信息处理
 * 4. 多语言支持（通过ResultCode枚举）
 * 5. Swagger文档集成
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
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

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 业务错误响应
     * 
     * @param resultCode 预定义错误码枚举
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return error(resultCode, ResultCode.Language.ZH);
    }

    /**
     * 多语言错误响应
     * 
     * @param resultCode 预定义错误码枚举
     * @param language 语言类型
     */
    public static <T> Result<T> error(ResultCode resultCode, ResultCode.Language language) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage(language));
        return result;
    }

    /**
     * 自定义错误响应
     * 
     * @param code 自定义状态码
     * @param message 自定义错误信息
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 分页响应
     * 
     * @param data 分页数据列表
     * @param total 总记录数
     * @param page 当前页码
     * @param size 每页数量
     */
    public static <T> Result<PageResult<T>> page(T data, long total, int page, int size) {
        PageResult<T> pageResult = new PageResult<>(data, total, page, size);
        return success(pageResult);
    }

    /**
     * 异常处理（开发环境显示详细信息）
     */
    public static <T> Result<T> error(Exception e) {
        return error(e, true);
    }

    /**
     * 异常处理（可控制是否显示详细信息）
     * 
     * @param showDetail 是否显示详细错误信息
     */
    public static <T> Result<T> error(Exception e, boolean showDetail) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.INTERNAL_ERROR.getCode());
        result.setMessage(showDetail ? e.getMessage() : ResultCode.INTERNAL_ERROR.getMessage());
        result.setDebugInfo(showDetail ? e.getClass().getName() : null);
        return result;
    }

    /**
     * HTTP状态码转换
     */
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

    /**
     * 分页结果内部类
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PageResult<T> {
        @Schema(description = "数据列表")
        private T list;
        
        @Schema(description = "总记录数")
        private long total;
        
        @Schema(description = "当前页码")
        private int page;
        
        @Schema(description = "每页数量")
        private int size;
        
        @Schema(description = "总页数")
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