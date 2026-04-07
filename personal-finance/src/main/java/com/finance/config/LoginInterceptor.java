package com.finance.config;

import com.finance.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        String token = authHeader.substring(7);
        if (!JwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}