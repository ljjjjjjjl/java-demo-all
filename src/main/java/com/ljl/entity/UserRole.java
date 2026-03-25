package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("user_role")
public class UserRole {
    private Long userId; // 用户ID
    private Long roleId; // 角色ID
}

