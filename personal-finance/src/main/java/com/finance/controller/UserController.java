package com.finance.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.finance.entity.User;
import com.finance.service.UserService;
import com.finance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        boolean success = userService.register(user.getUsername(), md5Password, user.getEmail());
        if (success) {
            return Result.success("注册成功");
        }
        return Result.error("用户名已存在");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        User loginUser = userService.login(user.getUsername(), md5Password);
        if (loginUser != null) {
            String token = JwtUtil.generateToken(loginUser.getId(), loginUser.getUsername());
            return Result.success("登录成功", token);
        }
        return Result.error("用户名或密码错误");
    }

    @GetMapping("/info")
    public Result info(@RequestHeader("Authorization") String token) {
        if (!JwtUtil.validateToken(token)) {
            return Result.error("Token 无效");
        }
        Long userId = JwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        return Result.success("获取成功", user);
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.success("退出成功");
    }
}