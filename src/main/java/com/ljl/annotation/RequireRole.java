package com.ljl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义权限注解：标注接口需要的角色
 */
// 注解作用在方法上（也可扩展到类上）
@Target({ElementType.METHOD})
// 注解在运行时生效（拦截器能读取）
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    // 支持单个/多个角色，默认空数组
    String[] value() default {};
}