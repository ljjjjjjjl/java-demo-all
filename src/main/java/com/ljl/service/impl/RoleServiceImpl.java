package com.ljl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljl.entity.Role;
import com.ljl.mapper.RoleMapper;
import com.ljl.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }
}

