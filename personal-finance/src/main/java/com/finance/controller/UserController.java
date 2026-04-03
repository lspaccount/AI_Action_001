package com.finance.controller;

import com.finance.entity.User;
import com.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user, HttpSession session) {
        boolean success = userService.register(user.getUsername(), user.getPassword(), user.getEmail());
        if (success) {
            session.setAttribute("userId", user.getId());
            return Result.success("注册成功");
        }
        return Result.error("用户名已存在");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            session.setAttribute("userId", loginUser.getId());
            return Result.success("登录成功");
        }
        return Result.error("用户名或密码错误");
    }

    @GetMapping("/info")
    public Result info(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录");
        }
        User user = userService.getById(userId);
        return Result.success("获取成功", user);
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }
}