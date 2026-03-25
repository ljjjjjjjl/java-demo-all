package com.ljl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data  // Lombok自动生成getter/setter/toString等方法
@TableName("user")  // 指定数据库表名
public class User {


        /**
         * 主键ID，自增
         */
        @TableId(type = IdType.AUTO)
        private Long id;

        /**
         * 用户名（唯一）
         */
        private String username;

        /**
         * 密码（后续会加密存储）
         */
        private String password;

        /**
         * 真实姓名
         */
        private String realName;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 状态：0正常 1禁用
         */
        private Integer status;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;
}
