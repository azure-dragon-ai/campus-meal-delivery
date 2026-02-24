package com.campus.business.admin.service;

import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;

import java.util.List;

/**
 * 后台管理服务接口
 */
public interface AdminService {

    /**
     * 管理员登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 管理员登出
     */
    void logout(Long adminId);

    /**
     * 获取管理员列表
     */
    PageResult<AdminVO> getAdminList(Integer page, Integer size, String keyword);

    /**
     * 创建管理员
     */
    Long createAdmin(CreateAdminRequest request);

    /**
     * 更新管理员
     */
    void updateAdmin(Long id, CreateAdminRequest request);

    /**
     * 删除管理员
     */
    void deleteAdmin(Long id);

    /**
     * 重置密码
     */
    void resetPassword(Long id, String newPassword);
}
