package com.ljl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljl.entity.User;

/**
 * 用户业务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户信息，失败返回null
     */

    // 根据用户名查询用户
    User getUserByUsername(String username);
    // 用户登录（校验密码，返回用户信息）
    User login(String username, String password);
    // 登出（拉黑Token）
    void logout(String token);
}