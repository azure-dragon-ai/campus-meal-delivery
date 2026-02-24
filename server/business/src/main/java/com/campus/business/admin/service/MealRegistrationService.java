package com.campus.business.admin.service;

import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;

/**
 * 配餐登记服务接口
 */
public interface MealRegistrationService {

    /**
     * 获取配餐登记列表
     */
    PageResult<MealRegistrationVO> getMealRegistrationList(Integer page, Integer size, Long semesterId, Long schoolId, String keyword);

    /**
     * 创建配餐登记
     */
    Long createMealRegistration(CreateMealRegistrationRequest request);

    /**
     * 更新配餐登记
     */
    void updateMealRegistration(Long id, CreateMealRegistrationRequest request);

    /**
     * 删除配餐登记
     */
    void deleteMealRegistration(Long id);

    /**
     * 批量导入配餐登记
     */
    void batchImport(Long semesterId, java.io.InputStream inputStream);
}
