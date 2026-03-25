package com.ljl.aspect;

import com.ljl.entity.OperationLog;
import com.ljl.mapper.OperationLogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面类
 * 使用 AOP 技术自动记录用户操作日志
 * @author ljl
 */
@Aspect
@Component
public class OperationLogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 切点定义：拦截所有标注了@OperationLog 注解的方法
     */
    @Pointcut("@annotation(com.ljl.annotation.OperationLog)")
    public void logPointcut() {}

    /**
     * 环绕通知：在目标方法执行前后记录日志
     * @param point 切点对象
     * @return 目标方法执行结果
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        
        // 获取 HTTP 请求上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("无法获取请求上下文");
        }
        
        HttpServletRequest request = attributes.getRequest();

        // 获取方法签名和自定义注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
        com.ljl.annotation.OperationLog annotation = method.getAnnotation(com.ljl.annotation.OperationLog.class);

        // 构建日志对象
        OperationLog log = new OperationLog();
        log.setUsername("admin"); // TODO: 从 JWT Token 中解析真实用户名
        log.setOperation(annotation.value());
        log.setMethod(request.getMethod() + " " + request.getRequestURI());
        log.setIp(getIpAddress(request));
        log.setCreateTime(LocalDateTime.now());

        try {
            Object result = point.proceed();
            log.setStatus(0); // 执行成功
            return result;
        } catch (Exception e) {
            log.setStatus(1); // 执行失败
            log.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            log.setCostTime(System.currentTimeMillis() - beginTime);
            operationLogMapper.insert(log); // 持久化到数据库
        }
    }

    /**
     * 获取客户端真实 IP 地址
     * @param request HTTP 请求对象
     * @return 客户端 IP
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
