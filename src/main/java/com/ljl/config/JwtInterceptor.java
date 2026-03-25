package com.ljl.config;

import com.ljl.annotation.RequireRole;
import com.ljl.exception.BusinessException;
import com.ljl.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT拦截器：校验除登录外的所有接口的Token合法性
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 1. 排除登录接口（不需要 Token）
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/user/login")) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 2. 从请求头获取 Token（格式：Bearer xxxxx）
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\",\"data\":null}");
            return false;
        }
        token = token.substring(7); // 去掉"Bearer "前缀
        
        // 3. 解析 Token，获取用户角色
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (BusinessException e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"" + e.getMessage() + "\",\"data\":null}");
            return false;
        }
        
        List<String> userRoles = (List<String>) claims.get("roles");
        System.out.println("用户角色：" + userRoles); // 看控制台输出
        if (userRoles == null || userRoles.isEmpty()) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"无角色权限\",\"data\":null}");
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 4. 读取 @RequireRole 注解，匹配角色
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole != null) {
            String[] requiredRoles = requireRole.value();
            // 检查用户角色是否包含至少一个要求的角色
            boolean hasPermission = false;
            if (userRoles != null) {
                hasPermission = Arrays.stream(requiredRoles)
                        .anyMatch(role -> userRoles.contains(role));
            }
            if (!hasPermission) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"无访问权限\",\"data\":null}");
                return false;
            }
        }

        // 5. 校验 Token（解析失败会抛异常，被全局异常处理器捕获）
        jwtUtil.parseToken(token);

        return true; // 校验通过，放行

    }
}