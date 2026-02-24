package com.campus.business.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.business.admin.service.MealRegistrationService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.database.entity.BizMealRegistration;
import com.campus.database.mapper.BizMealRegistrationMapper;
import com.campus.database.mapper.BizClassMapper;
import com.campus.database.mapper.BizSchoolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 配餐登记服务实现
 */
@Service
@RequiredArgsConstructor
public class MealRegistrationServiceImpl implements MealRegistrationService {

    private final BizMealRegistrationMapper bizMealRegistrationMapper;
    private final BizSchoolMapper bizSchoolMapper;
    private final BizClassMapper bizClassMapper;

    @Override
    public PageResult<MealRegistrationVO> getMealRegistrationList(Integer page, Integer size, Long semesterId, Long schoolId, String keyword) {
        Page<BizMealRegistration> regPage = new Page<>(page, size);
        LambdaQueryWrapper<BizMealRegistration> wrapper = new LambdaQueryWrapper<>();

        if (semesterId != null) {
            wrapper.eq(BizMealRegistration::getSemesterId, semesterId);
        }
        if (schoolId != null) {
            wrapper.eq(BizMealRegistration::getSchoolId, schoolId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w
                    .like(BizMealRegistration::getDinerName, keyword)
                    .or()
                    .like(BizMealRegistration::getIdCard, keyword)
                    .or()
                    .like(BizMealRegistration::getPhone, keyword));
        }
        wrapper.orderByDesc(BizMealRegistration::getCreateTime);

        Page<BizMealRegistration> result = bizMealRegistrationMapper.selectPage(regPage, wrapper);

        List<MealRegistrationVO> records = result.getRecords().stream().map(reg -> {
            MealRegistrationVO vo = new MealRegistrationVO();
            vo.setId(reg.getId());
            vo.setSemesterId(reg.getSemesterId());
            vo.setDinerId(reg.getDinerId());
            vo.setDinerName(reg.getDinerName());
            vo.setIdCard(reg.getIdCard());
            vo.setPhone(reg.getPhone());
            vo.setSchoolId(reg.getSchoolId());
            vo.setClassId(reg.getClassId());
            vo.setIsEat(reg.getIsEat());
            vo.setIsEatName(reg.getIsEat() == 1 ? "是" : "否");
            vo.setRemark(reg.getRemark());
            vo.setCreateTime(reg.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), size, page);
    }

    @Override
    @Transactional
    public Long createMealRegistration(CreateMealRegistrationRequest request) {
        // 检查是否已存在
        BizMealRegistration exist = bizMealRegistrationMapper.selectOne(new LambdaQueryWrapper<BizMealRegistration>()
                .eq(BizMealRegistration::getSemesterId, request.getSemesterId())
                .eq(BizMealRegistration::getDinerId, request.getDinerId()));
        if (exist != null) {
            throw new BusinessException("该就餐人在本学期的登记已存在");
        }

        BizMealRegistration reg = new BizMealRegistration();
        reg.setSemesterId(request.getSemesterId());
        reg.setDinerId(request.getDinerId());
        reg.setSchoolId(request.getSchoolId());
        reg.setClassId(request.getClassId());
        reg.setDinerName(request.getDinerName());
        reg.setIdCard(request.getIdCard());
        reg.setPhone(request.getPhone());
        reg.setIsEat(request.getIsEat());
        reg.setRemark(request.getRemark());

        bizMealRegistrationMapper.insert(reg);
        return reg.getId();
    }

    @Override
    @Transactional
    public void updateMealRegistration(Long id, CreateMealRegistrationRequest request) {
        BizMealRegistration reg = bizMealRegistrationMapper.selectById(id);
        if (reg == null) {
            throw new BusinessException("配餐登记不存在");
        }

        reg.setDinerName(request.getDinerName());
        reg.setIdCard(request.getIdCard());
        reg.setPhone(request.getPhone());
        reg.setIsEat(request.getIsEat());
        reg.setRemark(request.getRemark());

        bizMealRegistrationMapper.updateById(reg);
    }

    @Override
    @Transactional
    public void deleteMealRegistration(Long id) {
        bizMealRegistrationMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void batchImport(Long semesterId, java.io.InputStream inputStream) {
        // TODO: 实现 Excel 导入
    }
}
