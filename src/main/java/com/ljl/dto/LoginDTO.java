package com.ljl.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username; // 用户名
    @NotBlank(message = "密码不能为空")
    private String password; // 密码
}