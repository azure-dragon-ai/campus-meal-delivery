package com.campus.business.admin.controller;

import com.campus.business.admin.service.MealRegistrationService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 配餐登记 Controller
 */
@RestController
@RequestMapping("/api/meal-reg")
@RequiredArgsConstructor
public class MealRegistrationController {

    private final MealRegistrationService mealRegistrationService;

    /**
     * 获取配餐登记列表
     */
    @GetMapping("/list")
    public Result<PageResult<MealRegistrationVO>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String keyword) {
        return Result.success(mealRegistrationService.getMealRegistrationList(page, size, semesterId, schoolId, keyword));
    }

    /**
     * 创建配餐登记
     */
    @PostMapping
    public Result<Long> createMealRegistration(@Valid @RequestBody CreateMealRegistrationRequest request) {
        return Result.success(mealRegistrationService.createMealRegistration(request));
    }

    /**
     * 更新配餐登记
     */
    @PutMapping("/{id}")
    public Result<Void> updateMealRegistration(
            @PathVariable Long id,
            @Valid @RequestBody CreateMealRegistrationRequest request) {
        mealRegistrationService.updateMealRegistration(id, request);
        return Result.success();
    }

    /**
     * 删除配餐登记
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMealRegistration(@PathVariable Long id) {
        mealRegistrationService.deleteMealRegistration(id);
        return Result.success();
    }

    /**
     * 导入配餐登记
     */
    @PostMapping("/import")
    public Result<Void> importMealRegistration(
            @RequestParam Long semesterId,
            @RequestParam MultipartFile file) throws Exception {
        mealRegistrationService.batchImport(semesterId, file.getInputStream());
        return Result.success();
    }
}
