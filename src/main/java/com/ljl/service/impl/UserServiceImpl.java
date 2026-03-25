package com.ljl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljl.entity.User;
import com.ljl.exception.BusinessException;
import com.ljl.mapper.UserMapper;
import com.ljl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用户业务实现类
 */
@Service  // 标记为Spring服务组件
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 注入你已配置为Bean的BCrypt加密器
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        // 1. 根据用户名查询用户
        User user = this.getOne(new QueryWrapper<User>().eq("username", username));
        // 2. 用户名不存在校验
        if (user == null) {
            throw new BusinessException("用户名不存在"); // 抛自定义异常，替代返回null
        }

        //加密校验
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 校验用户状态（0正常，1禁用）
        if (user.getStatus() == 1) {
            throw new BusinessException("用户被禁用"); // 用户被禁用
        }

        return user;
    }

    @Override
    public void logout(String token) {
        // 将Token存入Redis黑名单，有效期和Token一致（假设Token有效期2小时）
        stringRedisTemplate.opsForValue().set(
                "blacklist:" + token,
                "1",
                2,
                TimeUnit.HOURS
        );

    }
}