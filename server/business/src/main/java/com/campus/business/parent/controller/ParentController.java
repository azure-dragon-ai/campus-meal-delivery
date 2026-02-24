package com.campus.business.parent.controller;

import com.campus.business.parent.service.ParentService;
import com.campus.business.parent.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 家长系统 Controller
 */
@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    /**
     * 家长登录
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody ParentLoginRequest request) {
        return Result.success(parentService.login(request));
    }

    /**
     * 家长注册
     */
    @PostMapping("/register")
    public Result<Long> register(@Valid @RequestBody ParentRegisterRequest request) {
        return Result.success(parentService.register(request));
    }

    /**
     * 获取就餐人列表
     */
    @GetMapping("/diners")
    public Result<List<DinerInfoVO>> getDiners(@RequestAttribute("userId") Long parentId) {
        return Result.success(parentService.getDiners(parentId));
    }

    /**
     * 创建就餐人
     */
    @PostMapping("/diners")
    public Result<Long> createDiner(
            @RequestAttribute("userId") Long parentId,
            @Valid @RequestBody CreateDinerRequest request) {
        return Result.success(parentService.createDiner(parentId, request));
    }

    /**
     * 更新就餐人
     */
    @PutMapping("/diners/{id}")
    public Result<Void> updateDiner(
            @PathVariable Long id,
            @Valid @RequestBody CreateDinerRequest request) {
        parentService.updateDiner(id, request);
        return Result.success();
    }

    /**
     * 删除就餐人
     */
    @DeleteMapping("/diners/{id}")
    public Result<Void> deleteDiner(@PathVariable Long id) {
        parentService.deleteDiner(id);
        return Result.success();
    }

    /**
     * 获取请假列表
     */
    @GetMapping("/leaves")
    public Result<PageResult<LeaveVO>> getLeaves(
            @RequestAttribute("userId") Long parentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(parentService.getLeaves(parentId, page, size));
    }

    /**
     * 创建请假
     */
    @PostMapping("/leaves")
    public Result<Long> createLeave(
            @RequestAttribute("userId") Long parentId,
            @Valid @RequestBody CreateLeaveRequest request) {
        return Result.success(parentService.createLeave(parentId, request));
    }

    /**
     * 取消请假
     */
    @PostMapping("/leaves/{id}/cancel")
    public Result<Void> cancelLeave(@PathVariable Long id) {
        parentService.cancelLeave(id);
        return Result.success();
    }
}
