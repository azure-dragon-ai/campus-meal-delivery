package com.campus.business.parent.service;

import com.campus.business.parent.vo.*;
import com.campus.core.common.PageResult;

import java.util.List;

/**
 * 家长服务接口
 */
public interface ParentService {

    /**
     * 家长登录
     */
    String login(ParentLoginRequest request);

    /**
     * 家长注册
     */
    Long register(ParentRegisterRequest request);

    /**
     * 获取就餐人列表
     */
    List<DinerInfoVO> getDiners(Long parentId);

    /**
     * 创建就餐人
     */
    Long createDiner(Long parentId, CreateDinerRequest request);

    /**
     * 更新就餐人
     */
    void updateDiner(Long dinerId, CreateDinerRequest request);

    /**
     * 删除就餐人
     */
    void deleteDiner(Long dinerId);

    /**
     * 获取请假列表
     */
    PageResult<LeaveVO> getLeaves(Long parentId, Integer page, Integer size);

    /**
     * 创建请假
     */
    Long createLeave(Long parentId, CreateLeaveRequest request);

    /**
     * 取消请假
     */
    void cancelLeave(Long leaveId);
}
