package com.example.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成工具类
 * 格式: 业务类型(2位) + 时间(yyMMddHHmmssSSS) + 序列号(4位) + 随机码(3位)
 * 示例: PC23120514301599900011234
 */
public class OrderNoUtils {
    private static final AtomicLong sequence = new AtomicLong(0);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    private static final int MAX_SEQUENCE = 9999;

    /**
     * 生成订单号
     *
     * @param bizType 业务类型 (PC:电脑端订单, AP:APP订单, WX:微信订单等)
     */
    public static String generate(String bizType) {
        // 17位时间戳
        String timePart = LocalDateTime.now().format(formatter);

        // 4位自增序列（循环使用）
        long seq = sequence.getAndIncrement() % (MAX_SEQUENCE + 1);
        if (sequence.get() > MAX_SEQUENCE) {
            sequence.set(0);
        }

        // 3位随机数
        int random = ThreadLocalRandom.current().nextInt(100, 999);

        return String.format("%s%s%04d%03d",
                bizType,
                timePart,
                seq,
                random
        );
    }
} 