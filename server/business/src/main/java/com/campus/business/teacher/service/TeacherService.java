package com.campus.business.teacher.service;

import com.campus.business.teacher.vo.*;
import com.campus.core.common.PageResult;

import java.util.List;

/**
 * 教师服务接口
 */
public interface TeacherService {

    /**
     * 教师登录
     */
    String login(TeacherLoginRequest request);

    /**
     * 教师注册
     */
    Long register(TeacherRegisterRequest request);

    /**
     * 获取当前教师信息
     */
    TeacherVO getCurrentTeacher(Long teacherId);

    /**
     * 查询班级就餐人列表
     */
    List<DinerVO> getClassDiners(Long teacherId);

    /**
     * 查询请假列表
     */
    PageResult<LeaveVO> getLeaveList(Long teacherId, Integer page, Integer size, Integer status);

    /**
     * 审核请假
     */
    void auditLeave(Long leaveId, Integer status, String remark);

    /**
     * 创建食谱
     */
    Long createRecipe(Long teacherId, TeacherCreateRecipeRequest request);

    /**
     * 获取食谱列表
     */
    PageResult<RecipeVO> getRecipeList(Long teacherId, Integer page, Integer size, Integer weekDay);
}
