package com.ljl.exception;

public class BusinessException extends RuntimeException {
    private Integer code;

    // 已有的构造器（仅 message）
    public BusinessException(String message) {
        super(message);
    }

    // 新增：code + message 构造器（匹配你调用的格式）
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}