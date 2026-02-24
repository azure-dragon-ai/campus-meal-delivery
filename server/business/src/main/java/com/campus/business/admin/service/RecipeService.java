package com.campus.business.admin.service;

import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;

/**
 * 食谱服务接口
 */
public interface RecipeService {

    /**
     * 获取食谱列表
     */
    PageResult<RecipeVO> getRecipeList(Integer page, Integer size, Long semesterId, Long schoolId, String startDate, String endDate);

    /**
     * 创建食谱
     */
    Long createRecipe(Long createBy, String createName, CreateRecipeRequest request);

    /**
     * 更新食谱
     */
    void updateRecipe(Long id, CreateRecipeRequest request);

    /**
     * 删除食谱
     */
    void deleteRecipe(Long id);

    /**
     * 按周获取食谱
     */
    java.util.Map<String, RecipeVO> getWeekRecipe(Long schoolId, java.time.LocalDate date);
}
