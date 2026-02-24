package com.campus.business.config;

import com.campus.core.constant.SystemConstants;
import com.campus.core.exception.UnauthorizedException;
import com.campus.core.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 认证拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 放行登录接口
        String uri = request.getRequestURI();
        if (uri.contains("/api/admin/login") ||
            uri.contains("/api/teacher/register") ||
            uri.contains("/api/teacher/login") ||
            uri.contains("/api/parent/register") ||
            uri.contains("/api/parent/login")) {
            return true;
        }

        // 获取 Token
        String token = request.getHeader(SystemConstants.JWT_HEADER);
        if (token == null || !token.startsWith(SystemConstants.JWT_PREFIX)) {
            throw new UnauthorizedException("请先登录");
        }

        token = token.substring(SystemConstants.JWT_PREFIX.length());

        // 验证 Token
        if (!JwtUtil.verify(token)) {
            throw new UnauthorizedException("登录已过期，请重新登录");
        }

        // 将用户信息放入请求头
        Long userId = JwtUtil.getUserId(token);
        String userType = JwtUtil.getUserType(token);
        request.setAttribute("userId", userId);
        request.setAttribute("userType", userType);

        // 放入请求头供后续使用
        response.setHeader("X-User-Id", String.valueOf(userId));
        response.setHeader("X-User-Type", userType);

        return true;
    }
}
