package com.ljl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
     * 用户数据访问接口，MyBatis Plus自动实现CRUD
     */
    @Mapper  // 标记为MyBatis Mapper接口
    public interface UserMapper extends BaseMapper<User> {

        /**
         * 根据用户名查询用户（登录时用）
         * @param username 用户名
         * @return 用户信息
         */
        @Select("select * from user where username = #{username}")
        User selectByUsername(String username);
}
