package com.finance.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.finance.entity.User;
import com.finance.service.UserService;
import com.finance.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册", description = "注册新用户")
    @PostMapping("/register")
    public Result<?> register(@Parameter(description = "用户信息") @RequestBody User user) {
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        boolean success = userService.register(user.getUsername(), md5Password, user.getEmail());
        if (success) {
            return Result.success("注册成功", null);
        }
        return Result.error("用户名已存在");
    }

    @Operation(summary = "用户登录", description = "登录获取Token")
    @PostMapping("/login")
    public Result<String> login(@Parameter(description = "用户登录信息") @RequestBody User user) {
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        User loginUser = userService.login(user.getUsername(), md5Password);
        if (loginUser != null) {
            String token = JwtUtil.generateToken(loginUser.getId(), loginUser.getUsername());
            return Result.success("登录成功", token);
        }
        return Result.error("用户名或密码错误");
    }

    @Operation(summary = "获取用户信息", description = "根据Token获取用户信息")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/info")
    public Result<User> info(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        if (!JwtUtil.validateToken(token)) {
            return Result.error("Token 无效");
        }
        Long userId = JwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        return Result.success("获取成功", user);
    }

    @Operation(summary = "用户退出", description = "用户退出登录")
    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出成功", null);
    }
}