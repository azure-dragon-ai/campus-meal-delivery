package com.campus.business.teacher.controller;

import com.campus.business.teacher.service.TeacherService;
import com.campus.business.teacher.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 教师系统 Controller
 */
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * 教师登录
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody TeacherLoginRequest request) {
        return Result.success(teacherService.login(request));
    }

    /**
     * 教师注册
     */
    @PostMapping("/register")
    public Result<Long> register(@Valid @RequestBody TeacherRegisterRequest request) {
        return Result.success(teacherService.register(request));
    }

    /**
     * 获取当前教师信息
     */
    @GetMapping("/info")
    public Result<TeacherVO> getInfo(@RequestAttribute("userId") Long teacherId) {
        return Result.success(teacherService.getCurrentTeacher(teacherId));
    }

    /**
     * 查询班级就餐人列表
     */
    @GetMapping("/diners")
    public Result<List<DinerVO>> getClassDiners(@RequestAttribute("userId") Long teacherId) {
        return Result.success(teacherService.getClassDiners(teacherId));
    }

    /**
     * 查询请假列表
     */
    @GetMapping("/leaves")
    public Result<PageResult<LeaveVO>> getLeaves(
            @RequestAttribute("userId") Long teacherId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return Result.success(teacherService.getLeaveList(teacherId, page, size, status));
    }

    /**
     * 审核请假
     */
    @PostMapping("/leaves/{id}/audit")
    public Result<Void> auditLeave(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        teacherService.auditLeave(id, status, remark);
        return Result.success();
    }
}
