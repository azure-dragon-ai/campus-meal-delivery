package com.campus.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.database.entity.BizMealRegistration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 配餐登记 Mapper
 */
@Mapper
public interface BizMealRegistrationMapper extends BaseMapper<BizMealRegistration> {
}
