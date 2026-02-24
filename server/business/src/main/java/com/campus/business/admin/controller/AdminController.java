package com.campus.business.admin.controller;

import com.campus.business.admin.service.AdminService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台管理 Controller
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(adminService.login(request));
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("X-User-Id") Long adminId) {
        adminService.logout(adminId);
        return Result.success();
    }

    /**
     * 获取管理员列表
     */
    @GetMapping("/list")
    public Result<PageResult<AdminVO>> getAdminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(adminService.getAdminList(page, size, keyword));
    }

    /**
     * 创建管理员
     */
    @PostMapping
    public Result<Long> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return Result.success(adminService.createAdmin(request));
    }

    /**
     * 更新管理员
     */
    @PutMapping("/{id}")
    public Result<Void> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody CreateAdminRequest request) {
        adminService.updateAdmin(id, request);
        return Result.success();
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return Result.success();
    }

    /**
     * 重置密码
     */
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        adminService.resetPassword(id, newPassword);
        return Result.success();
    }
}
