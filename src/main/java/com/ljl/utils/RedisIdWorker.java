package com.ljl.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Redis全局唯一ID生成器
 */
@Component
public class RedisIdWorker {
    // 起始时间戳：2020-01-01 00:00:00 UTC
    private static final long BEGIN_TIMESTAMP = 1609430400L;
    // 序列号位数
    private static final int COUNT_BITS = 32;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 生成ID的核心方法
    public long nextId(String keyPrefix) {
        // 1. 生成时间戳（秒级）
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2. 生成当日维度的序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3. 拼接时间戳和序列号
        return timestamp << COUNT_BITS | count;
    }

    // 测试方法（可直接运行）
    public static void main(String[] args) {
        // ========== 模拟Spring环境注入RedisTemplate（本地测试用） ==========
        // 实际项目中无需手动创建，Spring会自动注入
        org.springframework.data.redis.connection.RedisStandaloneConfiguration config =
                new org.springframework.data.redis.connection.RedisStandaloneConfiguration();
        config.setHostName("localhost"); // 你的Redis地址
        config.setPort(6379);
        // 如果Redis有密码，添加这行：config.setPassword("你的Redis密码");

        org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory factory =
                new org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory(config);
        factory.afterPropertiesSet();

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        // ========== 生成ID并打印 ==========
        RedisIdWorker idWorker = new RedisIdWorker(redisTemplate);

        // 生成5个订单ID测试
        System.out.println("===== 生成的全局唯一ID =====");
        for (int i = 0; i < 5; i++) {
            long orderId = idWorker.nextId("order");
            System.out.println("第" + (i+1) + "个ID：" + orderId);

            // 可选：拆分ID，查看时间戳和序列号（便于理解）
            long timestamp = orderId >> COUNT_BITS; // 右移32位，还原时间戳
            long count = orderId & ((1L << COUNT_BITS) - 1); // 取低32位，还原序列号
            // 还原具体时间
            LocalDateTime generateTime = LocalDateTime.ofEpochSecond(
                    timestamp + BEGIN_TIMESTAMP, 0, ZoneOffset.UTC
            );
            System.out.println("  → 生成时间：" + generateTime);
            System.out.println("  → 当日序列号：" + count);
            System.out.println("------------------------");
        }
    }
}