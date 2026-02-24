package com.campus.business.admin.controller;

import com.campus.business.admin.service.RecipeService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

/**
 * 食谱管理 Controller
 */
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * 获取食谱列表
     */
    @GetMapping("/list")
    public Result<PageResult<RecipeVO>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(recipeService.getRecipeList(page, size, semesterId, schoolId, startDate, endDate));
    }

    /**
     * 创建食谱
     */
    @PostMapping
    public Result<Long> createRecipe(
            @Valid @RequestBody CreateRecipeRequest request,
            HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        String userName = "管理员"; // 可从用户服务获取
        return Result.success(recipeService.createRecipe(userId, userName, request));
    }

    /**
     * 更新食谱
     */
    @PutMapping("/{id}")
    public Result<Void> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody CreateRecipeRequest request) {
        recipeService.updateRecipe(id, request);
        return Result.success();
    }

    /**
     * 删除食谱
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return Result.success();
    }

    /**
     * 获取周食谱
     */
    @GetMapping("/week")
    public Result<java.util.Map<String, RecipeVO>> getWeekRecipe(
            @RequestParam Long schoolId,
            @RequestParam(required = false) String date) {
        java.time.LocalDate targetDate = date != null ?
            java.time.LocalDate.parse(date) : java.time.LocalDate.now();
        return Result.success(recipeService.getWeekRecipe(schoolId, targetDate));
    }
}
