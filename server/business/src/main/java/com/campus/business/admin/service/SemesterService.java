package com.campus.business.admin.service;

import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;

import java.util.List;

/**
 * 学期管理服务接口
 */
public interface SemesterService {

    /**
     * 获取学期列表
     */
    PageResult<SemesterVO> getSemesterList(Integer page, Integer size, String keyword);

    /**
     * 获取所有启用的学期
     */
    List<SemesterVO> getAllActiveSemesters();

    /**
     * 获取当前学期
     */
    SemesterVO getCurrentSemester();

    /**
     * 创建学期
     */
    Long createSemester(CreateSemesterRequest request);

    /**
     * 更新学期
     */
    void updateSemester(Long id, CreateSemesterRequest request);

    /**
     * 删除学期
     */
    void deleteSemester(Long id);

    /**
     * 设置当前学期
     */
    void setCurrentSemester(Long id);
}
