package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单号生成策略（支持分布式环境）
 * 格式示例：PO20231030152345001（前缀 + 时间戳 + 序列号）
 */
public class OrderNoUtil {
    // 序列号生成器
    private static final AtomicLong sequence = new AtomicLong(0);
    // 最大序列号（3位）
    private static final int MAX_SEQUENCE = 999;
    // 最后时间戳缓存
    private static volatile String lastTimestamp = "";

    /**
     * 生成带前缀的订单号（默认策略）
     *
     * @param prefix 业务前缀 2-4位字母（如：PO-普通订单，MO-秒杀订单）
     */
    public static synchronized String generate(String prefix) {
        String timestamp = getCurrentTimestamp();

        synchronized (OrderNoUtil.class) {
            // 时间戳变化时重置序列号
            if (!timestamp.equals(lastTimestamp)) {
                lastTimestamp = timestamp;
                sequence.set(0);
            }

            // 生成序列号并自增
            long seq = sequence.incrementAndGet();
            if (seq > MAX_SEQUENCE) {
                seq = sequence.getAndSet(0);
            }

            return String.format("%s%s%03d",
                    prefix,
                    timestamp,
                    seq);
        }
    }

    /**
     * 生成带随机后缀的订单号（高并发推荐）
     * 格式：前缀 + 时间戳 + 4位随机数
     */
    public static String generateWithRandom(String prefix) {
        return prefix + getCurrentTimestamp() +
                String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    /**
     * 生成分布式环境安全订单号（需配置workerId）
     *
     * @param workerId 工作节点ID（0-31）
     */
    public static String generateDistributed(String prefix, int workerId) {
        long timestamp = System.currentTimeMillis();
        return String.format("%s%d%03d%04d",
                prefix,
                timestamp,
                workerId % 32,
                ThreadLocalRandom.current().nextInt(10000));
    }

    // 获取精确到秒的时间戳（17位）
    private static String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
} 