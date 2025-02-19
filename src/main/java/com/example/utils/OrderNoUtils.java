package com.example.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成工具类 - 提供高并发场景下的唯一订单号生成方案
 * 
 * <p>订单号格式：业务类型(2位) + 时间(yyMMddHHmmssSSS) + 序列号(4位) + 随机码(3位)
 * 示例：PC23120514301599900011234
 * 
 * <p>特性说明：
 * 1. 时间戳精度到毫秒级（17位）
 * 2. 自增序列循环使用（0-9999）
 * 3. 末尾随机数增强唯一性
 * 4. 支持多业务类型标识
 * 
 * @author 学生姓名
 * @version 1.0
 * @since 2023/06
 */
public class OrderNoUtils {
    
    /**
     * 自增序列（线程安全）
     */
    private static final AtomicLong sequence = new AtomicLong(0);
    
    /**
     * 时间格式化器（线程安全）
     */
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    
    /**
     * 最大序列值（含）
     */
    private static final int MAX_SEQUENCE = 9999;

    /**
     * 生成唯一订单号
     * 
     * @param bizType 业务类型标识（2位大写字母）
     *                示例：PC-电脑端, AP-APP端, WX-微信端
     * @return 23位唯一订单号字符串
     * 
     * @throws IllegalArgumentException 当业务类型不符合规范时抛出
     * 
     * @apiNote 生成逻辑：
     * 1. 获取当前时间戳（17位）
     * 2. 获取自增序列（4位，循环使用）
     * 3. 生成3位随机数（100-999）
     * 4. 拼接各部分生成最终订单号
     */
    public static String generate(String bizType) {
        // 参数校验
        if (bizType == null || bizType.length() != 2 || !bizType.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("业务类型必须为2位大写字母");
        }

        // 获取17位时间戳（年月日时分秒毫秒）
        String timePart = LocalDateTime.now().format(formatter);

        // 获取并更新自增序列（线程安全）
        long seq = sequence.getAndUpdate(current -> 
            (current >= MAX_SEQUENCE) ? 0 : current + 1
        );

        // 生成3位随机数（避免以0开头）
        int random = ThreadLocalRandom.current().nextInt(100, 1000);

        // 格式化拼接订单号
        return String.format("%s%s%04d%03d", 
                bizType, 
                timePart, 
                seq, 
                random
        );
    }
} 