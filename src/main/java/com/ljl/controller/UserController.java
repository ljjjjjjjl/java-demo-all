package com.ljl.controller;

import com.ljl.dto.LoginDTO;
import com.ljl.entity.Role;
import com.ljl.entity.User;
import com.ljl.service.RoleService;
import com.ljl.service.UserService;
import com.ljl.utils.JwtUtil;
import com.ljl.utils.Result;
import com.ljl.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器，处理用户相关接口
 */
@RestController  // 标记为REST接口控制器
@RequestMapping("/user")  // 接口统一前缀
@Tag(name = "用户管理", description = "用户登录、信息查询等接口")
public class UserController {

    @Resource  // 注入UserService
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Autowired
    private JwtUtil JwtUtil;
    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        // 1. 校验登录
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        // 2. 查询用户角色
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> roleNames = roles.stream().map(Role::getRoleName).toList();
        // 3. 生成JWT Token（包含用户ID和角色）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", roleNames);
        String token = JwtUtil.generateToken(claims);
        // 4. 返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        data.put("roles", roleNames);
        return Result.success(data);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result logout(@RequestHeader("Authorization") String token) {
        // 去掉Bearer前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.logout(token);
        return Result.success("登出成功");
    }

    @GetMapping("/info")
    @ApiOperation("获取当前用户信息")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 解析Token获取用户信息
        Map<String, Object> claims = JwtUtil.parseToken(token);
        String username = (String) claims.get("username");
        List<String> roles = (List<String>) claims.get("roles");
        // 组装返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("roles", roles);
        return Result.success(data);
    }
}