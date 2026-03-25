package com.ljl.exception;

import com.ljl.exception.BusinessException;
import com.ljl.utils.Result; // 你的通用响应类
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：统一返回错误信息，避免前端接收到异常堆栈
 */
@RestControllerAdvice // 标记为全局异常处理，且返回JSON
public class GlobalExceptionHandler {

    // 捕获自定义业务异常（登录失败、用户不存在等）
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getMessage()); // 用你的Result类返回错误信息
    }

    // 捕获其他所有异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace(); // 日志记录（生产环境建议用logback）
        return Result.fail("服务器内部错误");
    }
}