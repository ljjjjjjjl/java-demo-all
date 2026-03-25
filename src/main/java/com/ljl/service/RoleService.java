package com.ljl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljl.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    // 根据用户ID查询角色列表
    List<Role> getRolesByUserId(Long userId);
}
