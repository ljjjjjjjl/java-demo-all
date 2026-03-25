package com.ljl.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户视图对象，只返回非敏感信息
 */
@Data
public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
}