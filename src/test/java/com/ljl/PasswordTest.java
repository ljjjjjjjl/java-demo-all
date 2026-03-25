package com.ljl;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    @Test
    public void testPasswordMatch() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "123456";
        // 数据库中的密码
        String encodedPassword = "$2a$10$7JB720yubVSbXyM9vLzYe.duYrcvXF7un705FhDDeSiid01qvf1Hu";

        System.out.println("原始密码：" + rawPassword);
        System.out.println("数据库密码：" + encodedPassword);

        // 验证匹配
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码匹配结果：" + matches);

        if (!matches) {
            System.out.println("密码不匹配！需要重新生成");
            String newEncoded = encoder.encode(rawPassword);
            System.out.println("新生成的密码：" + newEncoded);
        } else {
            System.out.println("密码匹配成功！");
        }
    }
}

