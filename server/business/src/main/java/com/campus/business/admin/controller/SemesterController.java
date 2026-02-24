package com.campus.business.admin.controller;

import com.campus.business.admin.service.SemesterService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 学期管理 Controller
 */
@RestController
@RequestMapping("/api/semester")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    /**
     * 获取学期列表
     */
    @GetMapping("/list")
    public Result<PageResult<SemesterVO>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(semesterService.getSemesterList(page, size, keyword));
    }

    /**
     * 获取所有启用的学期
     */
    @GetMapping("/active")
    public Result<List<SemesterVO>> getActiveSemesters() {
        return Result.success(semesterService.getAllActiveSemesters());
    }

    /**
     * 获取当前学期
     */
    @GetMapping("/current")
    public Result<SemesterVO> getCurrentSemester() {
        return Result.success(semesterService.getCurrentSemester());
    }

    /**
     * 创建学期
     */
    @PostMapping
    public Result<Long> createSemester(@Valid @RequestBody CreateSemesterRequest request) {
        return Result.success(semesterService.createSemester(request));
    }

    /**
     * 更新学期
     */
    @PutMapping("/{id}")
    public Result<Void> updateSemester(
            @PathVariable Long id,
            @Valid @RequestBody CreateSemesterRequest request) {
        semesterService.updateSemester(id, request);
        return Result.success();
    }

    /**
     * 删除学期
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return Result.success();
    }

    /**
     * 设置当前学期
     */
    @PutMapping("/{id}/current")
    public Result<Void> setCurrentSemester(@PathVariable Long id) {
        semesterService.setCurrentSemester(id);
        return Result.success();
    }
}
