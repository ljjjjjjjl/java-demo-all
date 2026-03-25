package com.ljl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTesT {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testString() {
        System.out.println("=== 开始Redis测试 ===");

        try {
            // 写入数据
            redisTemplate.opsForValue().set("test_key", "ljl_redis_test");

            // 读取数据
            String value = (String) redisTemplate.opsForValue().get("test_key");
            System.out.println("读取到的数据: " + value);

            if ("ljl_redis_test".equals(value)) {
                System.out.println("✅ Redis字符串操作测试成功！");
            } else {
                System.out.println("❌ 数据不匹配");
            }

        } catch (Exception e) {
            System.err.println("❌ Redis测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
