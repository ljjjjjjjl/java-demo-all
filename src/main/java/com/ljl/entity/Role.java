package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;         // 主键
    private String roleName; // 角色名（ADMIN/OPERATOR）
    private String description; // 角色描述
}