package com.campus.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.database.entity.BizRecipe;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食谱 Mapper
 */
@Mapper
public interface BizRecipeMapper extends BaseMapper<BizRecipe> {
}
