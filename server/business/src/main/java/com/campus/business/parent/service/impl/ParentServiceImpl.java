package com.campus.business.parent.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.business.parent.service.ParentService;
import com.campus.business.parent.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.core.util.JwtUtil;
import com.campus.core.util.PasswordUtil;
import com.campus.database.entity.BizClass;
import com.campus.database.entity.BizDiner;
import com.campus.database.entity.BizLeave;
import com.campus.database.entity.BizMealRegistration;
import com.campus.database.entity.BizParent;
import com.campus.database.entity.BizSemester;
import com.campus.database.mapper.BizClassMapper;
import com.campus.database.mapper.BizDinerMapper;
import com.campus.database.mapper.BizLeaveMapper;
import com.campus.database.mapper.BizMealRegistrationMapper;
import com.campus.database.mapper.BizParentMapper;
import com.campus.database.mapper.BizSemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 家长服务实现
 */
@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final BizParentMapper bizParentMapper;
    private final BizDinerMapper bizDinerMapper;
    private final BizLeaveMapper bizLeaveMapper;
    private final BizClassMapper bizClassMapper;
    private final BizMealRegistrationMapper bizMealRegistrationMapper;
    private final BizSemesterMapper bizSemesterMapper;

    @Override
    public String login(ParentLoginRequest request) {
        BizParent parent = bizParentMapper.selectOne(new LambdaQueryWrapper<BizParent>()
                .eq(BizParent::getPhone, request.getPhone()));

        if (parent == null) {
            throw new BusinessException("用户不存在");
        }

        if (!PasswordUtil.matches(request.getPassword(), parent.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (parent.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", parent.getId());
        payload.put("userType", "PARENT");
        return JwtUtil.createToken(payload);
    }

    @Override
    @Transactional
    public Long register(ParentRegisterRequest request) {
        BizParent exist = bizParentMapper.selectOne(new LambdaQueryWrapper<BizParent>()
                .eq(BizParent::getPhone, request.getPhone()));
        if (exist != null) {
            throw new BusinessException("手机号已注册");
        }

        BizParent parent = new BizParent();
        parent.setName(request.getName());
        parent.setPhone(request.getPhone());
        parent.setPassword(PasswordUtil.encode(request.getPassword()));
        parent.setStatus(1);

        bizParentMapper.insert(parent);
        return parent.getId();
    }

    @Override
    public List<DinerInfoVO> getDiners(Long parentId) {
        List<BizDiner> diners = bizDinerMapper.selectList(new LambdaQueryWrapper<BizDiner>()
                .eq(BizDiner::getParentId, parentId)
                .eq(BizDiner::getStatus, 1));

        return diners.stream().map(diner -> {
            DinerInfoVO vo = new DinerInfoVO();
            vo.setId(diner.getId());
            vo.setParentId(diner.getParentId());
            vo.setName(diner.getName());
            vo.setRole(diner.getRole());
            vo.setRoleName(getRoleName(diner.getRole()));
            vo.setIdCard(diner.getIdCard());
            vo.setPhone(diner.getPhone());
            vo.setSchoolId(diner.getSchoolId());
            vo.setClassId(diner.getClassId());
            vo.setBankAccountName(diner.getBankAccountName());
            vo.setBankName(diner.getBankName());
            vo.setBankSubbranch(diner.getBankSubbranch());
            vo.setBankAccount(diner.getBankAccount());
            vo.setStatus(diner.getStatus());

            // 获取班级名称
            if (diner.getClassId() != null) {
                BizClass bizClass = bizClassMapper.selectById(diner.getClassId());
                if (bizClass != null) {
                    vo.setClassName(bizClass.getName());
                }
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createDiner(Long parentId, CreateDinerRequest request) {
        BizDiner diner = new BizDiner();
        diner.setParentId(parentId);
        diner.setSchoolId(request.getSchoolId());
        diner.setClassId(request.getClassId());
        diner.setName(request.getName());
        diner.setRole(request.getRole());
        diner.setIdCard(request.getIdCard());
        diner.setPhone(request.getPhone());
        diner.setBankAccountName(request.getBankAccountName());
        diner.setBankName(request.getBankName());
        diner.setBankSubbranch(request.getBankSubbranch());
        diner.setBankAccount(request.getBankAccount());
        diner.setStatus(1);

        bizDinerMapper.insert(diner);
        return diner.getId();
    }

    @Override
    @Transactional
    public void updateDiner(Long dinerId, CreateDinerRequest request) {
        BizDiner diner = bizDinerMapper.selectById(dinerId);
        if (diner == null) {
            throw new BusinessException("就餐人不存在");
        }

        diner.setName(request.getName());
        diner.setRole(request.getRole());
        diner.setIdCard(request.getIdCard());
        diner.setPhone(request.getPhone());
        diner.setBankAccountName(request.getBankAccountName());
        diner.setBankName(request.getBankName());
        diner.setBankSubbranch(request.getBankSubbranch());
        diner.setBankAccount(request.getBankAccount());

        bizDinerMapper.updateById(diner);
    }

    @Override
    @Transactional
    public void deleteDiner(Long dinerId) {
        BizDiner diner = bizDinerMapper.selectById(dinerId);
        if (diner == null) {
            throw new BusinessException("就餐人不存在");
        }

        diner.setStatus(0);
        bizDinerMapper.updateById(diner);
    }

    @Override
    public PageResult<LeaveVO> getLeaves(Long parentId, Integer page, Integer size) {
        // 获取家长的就餐人 ID 列表
        List<Long> dinerIds = bizDinerMapper.selectList(new LambdaQueryWrapper<BizDiner>()
                        .eq(BizDiner::getParentId, parentId))
                .stream().map(BizDiner::getId).collect(Collectors.toList());

        if (dinerIds.isEmpty()) {
            return PageResult.of(new ArrayList<>(), 0L, size, page);
        }

        Page<BizLeave> leavePage = new Page<>(page, size);
        LambdaQueryWrapper<BizLeave> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BizLeave::getDinerId, dinerIds);
        wrapper.orderByDesc(BizLeave::getCreateTime);

        Page<BizLeave> result = bizLeaveMapper.selectPage(leavePage, wrapper);

        List<LeaveVO> records = result.getRecords().stream().map(leave -> {
            LeaveVO vo = new LeaveVO();
            vo.setId(leave.getId());
            vo.setDinerId(leave.getDinerId());
            vo.setDinerName(leave.getDinerName());
            vo.setPhone(leave.getPhone());
            vo.setLeaveDate(leave.getLeaveDate());
            vo.setReason(leave.getReason());
            vo.setStatus(leave.getStatus());
            vo.setStatusName(getLeaveStatusName(leave.getStatus()));
            vo.setAuditTime(leave.getAuditTime());
            vo.setAuditRemark(leave.getAuditRemark());
            vo.setCreateTime(leave.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), size, page);
    }

    @Override
    @Transactional
    public Long createLeave(Long parentId, CreateLeaveRequest request) {
        BizDiner diner = bizDinerMapper.selectById(request.getDinerId());
        if (diner == null) {
            throw new BusinessException("就餐人不存在");
        }

        // 验证就餐人属于当前家长
        if (!diner.getParentId().equals(parentId)) {
            throw new BusinessException("无权操作该就餐人");
        }

        BizLeave leave = new BizLeave();
        leave.setDinerId(diner.getId());
        leave.setDinerName(diner.getName());
        leave.setPhone(StrUtil.isNotBlank(diner.getPhone()) ? diner.getPhone() : diner.getBankAccountName());
        leave.setLeaveDate(request.getLeaveDate());
        leave.setReason(request.getReason());
        leave.setStatus(0);

        bizLeaveMapper.insert(leave);
        return leave.getId();
    }

    @Override
    @Transactional
    public void cancelLeave(Long leaveId) {
        BizLeave leave = bizLeaveMapper.selectById(leaveId);
        if (leave == null) {
            throw new BusinessException("请假记录不存在");
        }

        if (leave.getStatus() != 0) {
            throw new BusinessException("只能取消待审核的请假");
        }

        leave.setStatus(2);
        leave.setAuditRemark("用户取消");
        leave.setAuditTime(LocalDateTime.now());

        bizLeaveMapper.updateById(leave);
    }

    private String getRoleName(Integer role) {
        if (role == 1) return "学生";
        if (role == 2) return "教师";
        if (role == 3) return "校干";
        return "未知";
    }

    private String getLeaveStatusName(Integer status) {
        if (status == 0) return "待审核";
        if (status == 1) return "通过";
        if (status == 2) return "拒绝";
        return "未知";
    }

    @Override
    public PageResult<MealRegistrationVO> getMealRegistrations(Long parentId, Integer page, Integer size, Long semesterId) {
        // 获取家长的就餐人 ID 列表
        List<Long> dinerIds = bizDinerMapper.selectList(new LambdaQueryWrapper<BizDiner>()
                        .eq(BizDiner::getParentId, parentId))
                .stream().map(BizDiner::getId).collect(Collectors.toList());

        if (dinerIds.isEmpty()) {
            return PageResult.of(new ArrayList<>(), 0L, size, page);
        }

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BizMealRegistration> regPage =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        LambdaQueryWrapper<BizMealRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BizMealRegistration::getDinerId, dinerIds);
        if (semesterId != null) {
            wrapper.eq(BizMealRegistration::getSemesterId, semesterId);
        }
        wrapper.orderByDesc(BizMealRegistration::getCreateTime);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BizMealRegistration> result =
            bizMealRegistrationMapper.selectPage(regPage, wrapper);

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
    public Long createMealRegistration(Long parentId, CreateMealRegistrationRequest request) {
        // 检查就餐人是否属于当前家长
        BizDiner diner = bizDinerMapper.selectById(request.getDinerId());
        if (diner == null) {
            throw new BusinessException("就餐人不存在");
        }
        if (!diner.getParentId().equals(parentId)) {
            throw new BusinessException("无权操作该就餐人");
        }

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
    public List<SemesterVO> getSemesters() {
        List<BizSemester> semesters = bizSemesterMapper.selectList(new LambdaQueryWrapper<BizSemester>()
                .eq(BizSemester::getStatus, 1)
                .orderByDesc(BizSemester::getCreateTime));
        return semesters.stream().map(semester -> {
            SemesterVO vo = new SemesterVO();
            vo.setId(semester.getId());
            vo.setName(semester.getName());
            vo.setStartDate(semester.getStartDate());
            vo.setEndDate(semester.getEndDate());
            vo.setStatus(semester.getStatus());
            vo.setIsCurrent(semester.getIsCurrent());
            return vo;
        }).collect(Collectors.toList());
    }
}
