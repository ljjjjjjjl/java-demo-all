package com.ljl.utils;

import com.ljl.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类：生成Token、解析Token、验证有效期
 */
@Component
public class JwtUtil {

    // ========== 1. 类成员变量（@Value必须写在这里） ==========
    // JWT密钥（配置在application.yml，至少32位）
    @Value("${jwt.secret:abcdefghijklmnopqrstuvwxyz1234567890abcdef}")
    private String secret;

    // Token有效期：2小时（7200000毫秒）
    @Value("${jwt.expire:7200000}")
    private long expire;

    // ========== 2. 生成Token ==========
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) // 包含userId、username、roles
                .setSubject((String) claims.get("username"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ========== 3. 解析Token（校验合法性+有效期） ==========
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey()) // 设置验签密钥
                    .build()
                    .parseClaimsJws(token) // 解析Token
                    .getBody(); // 获取Token中的Claims数据
        } catch (ExpiredJwtException e) {
            // 匹配你自定义的BusinessException构造器（code + message）
            throw new BusinessException(401, "Token已过期");
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(401, "Token无效或已篡改");
        }
    }


    // ========== 4. 获取签名密钥（私有方法） ==========
    private Key getSignKey() {
        // 把字符串密钥转为HS256所需的Key对象
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}