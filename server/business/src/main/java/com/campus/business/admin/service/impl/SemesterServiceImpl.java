package com.campus.business.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.business.admin.service.SemesterService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.database.entity.BizSemester;
import com.campus.database.mapper.BizSemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 学期服务实现
 */
@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final BizSemesterMapper bizSemesterMapper;

    @Override
    public PageResult<SemesterVO> getSemesterList(Integer page, Integer size, String keyword) {
        Page<BizSemester> semesterPage = new Page<>(page, size);
        LambdaQueryWrapper<BizSemester> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(BizSemester::getName, keyword);
        }
        wrapper.orderByDesc(BizSemester::getCreateTime);

        Page<BizSemester> result = bizSemesterMapper.selectPage(semesterPage, wrapper);

        List<SemesterVO> records = result.getRecords().stream().map(semester -> {
            SemesterVO vo = new SemesterVO();
            vo.setId(semester.getId());
            vo.setName(semester.getName());
            vo.setStartDate(semester.getStartDate());
            vo.setEndDate(semester.getEndDate());
            vo.setStatus(semester.getStatus());
            vo.setStatusName(semester.getStatus() == 1 ? "启用" : "停用");
            vo.setIsCurrent(semester.getIsCurrent());
            vo.setCurrentName(semester.getIsCurrent() == 1 ? "是" : "否");
            vo.setCreateTime(semester.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), size, page);
    }

    @Override
    public List<SemesterVO> getAllActiveSemesters() {
        List<BizSemester> semesters = bizSemesterMapper.selectList(new LambdaQueryWrapper<BizSemester>()
                .eq(BizSemester::getStatus, 1)
                .orderByDesc(BizSemester::getCreateTime));
        return semesters.stream().map(semester -> {
            SemesterVO vo = new SemesterVO();
            vo.setId(semester.getId());
            vo.setName(semester.getName());
            vo.setStartDate(semester.getStartDate());
            vo.setEndDate(semester.getEndDate());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public SemesterVO getCurrentSemester() {
        BizSemester semester = bizSemesterMapper.selectOne(new LambdaQueryWrapper<BizSemester>()
                .eq(BizSemester::getIsCurrent, 1)
                .eq(BizSemester::getStatus, 1)
                .orderByDesc(BizSemester::getCreateTime)
                .last("LIMIT 1"));
        if (semester == null) {
            return null;
        }
        SemesterVO vo = new SemesterVO();
        vo.setId(semester.getId());
        vo.setName(semester.getName());
        vo.setStartDate(semester.getStartDate());
        vo.setEndDate(semester.getEndDate());
        return vo;
    }

    @Override
    @Transactional
    public Long createSemester(CreateSemesterRequest request) {
        BizSemester semester = new BizSemester();
        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        semester.setIsCurrent(request.getIsCurrent() != null ? request.getIsCurrent() : 0);

        // 如果是当前学期，先将其他学期设置为非当前
        if (semester.getIsCurrent() == 1) {
            bizSemesterMapper.update(null, new LambdaQueryWrapper<BizSemester>()
                    .set(BizSemester::getIsCurrent, 0));
        }

        bizSemesterMapper.insert(semester);
        return semester.getId();
    }

    @Override
    @Transactional
    public void updateSemester(Long id, CreateSemesterRequest request) {
        BizSemester semester = bizSemesterMapper.selectById(id);
        if (semester == null) {
            throw new BusinessException("学期不存在");
        }

        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            semester.setStatus(request.getStatus());
        }

        // 如果是当前学期，先将其他学期设置为非当前
        if (request.getIsCurrent() != null && request.getIsCurrent() == 1) {
            bizSemesterMapper.update(null, new LambdaQueryWrapper<BizSemester>()
                    .ne(BizSemester::getId, id)
                    .set(BizSemester::getIsCurrent, 0));
            semester.setIsCurrent(1);
        }

        bizSemesterMapper.updateById(semester);
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        bizSemesterMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void setCurrentSemester(Long id) {
        BizSemester semester = bizSemesterMapper.selectById(id);
        if (semester == null) {
            throw new BusinessException("学期不存在");
        }

        // 先将所有学期设置为非当前
        bizSemesterMapper.update(null, new LambdaQueryWrapper<BizSemester>()
                .set(BizSemester::getIsCurrent, 0));

        // 设置当前学期
        semester.setIsCurrent(1);
        bizSemesterMapper.updateById(semester);
    }
}
