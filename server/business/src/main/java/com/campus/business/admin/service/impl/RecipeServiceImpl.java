package com.campus.business.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.business.admin.service.RecipeService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.database.entity.BizRecipe;
import com.campus.database.mapper.BizRecipeMapper;
import com.campus.database.mapper.BizSchoolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 食谱服务实现
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final BizRecipeMapper bizRecipeMapper;
    private final BizSchoolMapper bizSchoolMapper;

    private static final String[] WEEK_DAY_NAMES = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @Override
    public PageResult<RecipeVO> getRecipeList(Integer page, Integer size, Long semesterId, Long schoolId, String startDate, String endDate) {
        Page<BizRecipe> recipePage = new Page<>(page, size);
        LambdaQueryWrapper<BizRecipe> wrapper = new LambdaQueryWrapper<>();

        if (semesterId != null) {
            wrapper.eq(BizRecipe::getSemesterId, semesterId);
        }
        if (schoolId != null) {
            wrapper.eq(BizRecipe::getSchoolId, schoolId);
        }
        if (StrUtil.isNotBlank(startDate)) {
            wrapper.ge(BizRecipe::getDate, startDate);
        }
        if (StrUtil.isNotBlank(endDate)) {
            wrapper.le(BizRecipe::getDate, endDate);
        }
        wrapper.orderByAsc(BizRecipe::getDate);

        Page<BizRecipe> result = bizRecipeMapper.selectPage(recipePage, wrapper);

        List<RecipeVO> records = result.getRecords().stream().map(recipe -> {
            RecipeVO vo = new RecipeVO();
            vo.setId(recipe.getId());
            vo.setSemesterId(recipe.getSemesterId());
            vo.setSchoolId(recipe.getSchoolId());
            vo.setDate(recipe.getDate());
            vo.setWeekDay(recipe.getWeekDay());
            vo.setWeekDayName(WEEK_DAY_NAMES[recipe.getWeekDay()]);
            vo.setLunchMenu(recipe.getLunchMenu());
            vo.setLunchMenuWithWeight(recipe.getLunchMenuWithWeight());
            vo.setDinnerMenu(recipe.getDinnerMenu());
            vo.setDinnerMenuWithWeight(recipe.getDinnerMenuWithWeight());
            vo.setCreateBy(recipe.getCreateBy());
            vo.setCreateName(recipe.getCreateName());
            vo.setCreateTime(recipe.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), size, page);
    }

    @Override
    @Transactional
    public Long createRecipe(Long createBy, String createName, CreateRecipeRequest request) {
        // 检查是否已存在
        BizRecipe exist = bizRecipeMapper.selectOne(new LambdaQueryWrapper<BizRecipe>()
                .eq(BizRecipe::getSemesterId, request.getSemesterId())
                .eq(BizRecipe::getSchoolId, request.getSchoolId())
                .eq(BizRecipe::getDate, request.getDate()));
        if (exist != null) {
            throw new BusinessException("该日期的食谱已存在");
        }

        BizRecipe recipe = new BizRecipe();
        recipe.setSemesterId(request.getSemesterId());
        recipe.setSchoolId(request.getSchoolId());
        recipe.setDate(request.getDate());
        recipe.setWeekDay(request.getWeekDay());
        recipe.setLunchMenu(request.getLunchMenu());
        recipe.setLunchMenuWithWeight(request.getLunchMenuWithWeight());
        recipe.setDinnerMenu(request.getDinnerMenu());
        recipe.setDinnerMenuWithWeight(request.getDinnerMenuWithWeight());
        recipe.setCreateBy(createBy);
        recipe.setCreateName(createName);

        bizRecipeMapper.insert(recipe);
        return recipe.getId();
    }

    @Override
    @Transactional
    public void updateRecipe(Long id, CreateRecipeRequest request) {
        BizRecipe recipe = bizRecipeMapper.selectById(id);
        if (recipe == null) {
            throw new BusinessException("食谱不存在");
        }

        recipe.setLunchMenu(request.getLunchMenu());
        recipe.setLunchMenuWithWeight(request.getLunchMenuWithWeight());
        recipe.setDinnerMenu(request.getDinnerMenu());
        recipe.setDinnerMenuWithWeight(request.getDinnerMenuWithWeight());

        bizRecipeMapper.updateById(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        bizRecipeMapper.deleteById(id);
    }

    @Override
    public Map<String, RecipeVO> getWeekRecipe(Long schoolId, LocalDate date) {
        Map<String, RecipeVO> result = new LinkedHashMap<>();

        // 获取周一日期
        WeekFields weekFields = WeekFields.of(java.util.Locale.CHINA);
        LocalDate monday = date.with(java.time.DayOfWeek.MONDAY);

        for (int i = 0; i < 5; i++) {
            LocalDate current = monday.plusDays(i);
            String key = current.toString();
            result.put(key, null);
        }

        // 查询本周食谱
        List<BizRecipe> recipes = bizRecipeMapper.selectList(new LambdaQueryWrapper<BizRecipe>()
                .eq(BizRecipe::getSchoolId, schoolId)
                .between(BizRecipe::getDate, monday, monday.plusDays(4)));

        for (BizRecipe recipe : recipes) {
            result.put(recipe.getDate().toString(), new RecipeVO());
        }

        return result;
    }
}
