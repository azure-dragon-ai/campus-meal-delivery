package com.campus.business.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.business.teacher.service.TeacherService;
import com.campus.business.teacher.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.core.util.JwtUtil;
import com.campus.core.util.PasswordUtil;
import com.campus.database.entity.BizClass;
import com.campus.database.entity.BizDiner;
import com.campus.database.entity.BizLeave;
import com.campus.database.entity.BizTeacher;
import com.campus.database.mapper.BizClassMapper;
import com.campus.database.mapper.BizDinerMapper;
import com.campus.database.mapper.BizLeaveMapper;
import com.campus.database.mapper.BizTeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 教师服务实现
 */
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final BizTeacherMapper bizTeacherMapper;
    private final BizClassMapper bizClassMapper;
    private final BizDinerMapper bizDinerMapper;
    private final BizLeaveMapper bizLeaveMapper;

    @Override
    public String login(TeacherLoginRequest request) {
        BizTeacher teacher = bizTeacherMapper.selectOne(new LambdaQueryWrapper<BizTeacher>()
                .eq(BizTeacher::getPhone, request.getPhone()));

        if (teacher == null) {
            throw new BusinessException("用户不存在");
        }

        if (!PasswordUtil.matches(request.getPassword(), teacher.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (teacher.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", teacher.getId());
        payload.put("userType", "TEACHER");
        return JwtUtil.createToken(payload);
    }

    @Override
    @Transactional
    public Long register(TeacherRegisterRequest request) {
        BizTeacher exist = bizTeacherMapper.selectOne(new LambdaQueryWrapper<BizTeacher>()
                .eq(BizTeacher::getPhone, request.getPhone()));
        if (exist != null) {
            throw new BusinessException("手机号已注册");
        }

        BizTeacher teacher = new BizTeacher();
        teacher.setName(request.getName());
        teacher.setPhone(request.getPhone());
        teacher.setPassword(PasswordUtil.encode(request.getPassword()));
        teacher.setSchoolId(request.getSchoolId());
        teacher.setClassId(request.getClassId());
        teacher.setPosition(request.getPosition());
        teacher.setStatus(1);

        bizTeacherMapper.insert(teacher);
        return teacher.getId();
    }

    @Override
    public TeacherVO getCurrentTeacher(Long teacherId) {
        BizTeacher teacher = bizTeacherMapper.selectById(teacherId);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }

        TeacherVO vo = new TeacherVO();
        vo.setId(teacher.getId());
        vo.setName(teacher.getName());
        vo.setPhone(teacher.getPhone());
        vo.setSchoolId(teacher.getSchoolId());
        vo.setClassId(teacher.getClassId());
        vo.setPosition(teacher.getPosition());
        vo.setStatus(teacher.getStatus());
        vo.setCreateTime(teacher.getCreateTime());

        // 获取学校名称
        if (teacher.getSchoolId() != null) {
            // 此处可关联查询学校表
        }
        // 获取班级名称
        if (teacher.getClassId() != null) {
            BizClass bizClass = bizClassMapper.selectById(teacher.getClassId());
            if (bizClass != null) {
                vo.setClassName(bizClass.getName());
            }
        }

        return vo;
    }

    @Override
    public List<DinerVO> getClassDiners(Long teacherId) {
        BizTeacher teacher = bizTeacherMapper.selectById(teacherId);
        if (teacher == null || teacher.getClassId() == null) {
            return new ArrayList<>();
        }

        List<BizDiner> diners = bizDinerMapper.selectList(new LambdaQueryWrapper<BizDiner>()
                .eq(BizDiner::getClassId, teacher.getClassId())
                .eq(BizDiner::getStatus, 1));

        return diners.stream().map(diner -> {
            DinerVO vo = new DinerVO();
            vo.setId(diner.getId());
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
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public PageResult<LeaveVO> getLeaveList(Long teacherId, Integer page, Integer size, Integer status) {
        BizTeacher teacher = bizTeacherMapper.selectById(teacherId);
        if (teacher == null || teacher.getClassId() == null) {
            return PageResult.of(new ArrayList<>(), 0L, size, page);
        }

        // 获取班级就餐人 ID 列表
        List<Long> dinerIds = bizDinerMapper.selectList(new LambdaQueryWrapper<BizDiner>()
                        .eq(BizDiner::getClassId, teacher.getClassId()))
                .stream().map(BizDiner::getId).collect(Collectors.toList());

        if (dinerIds.isEmpty()) {
            return PageResult.of(new ArrayList<>(), 0L, size, page);
        }

        Page<BizLeave> leavePage = new Page<>(page, size);
        LambdaQueryWrapper<BizLeave> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BizLeave::getDinerId, dinerIds);
        if (status != null) {
            wrapper.eq(BizLeave::getStatus, status);
        }
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
    public void auditLeave(Long leaveId, Integer status, String remark) {
        BizLeave leave = bizLeaveMapper.selectById(leaveId);
        if (leave == null) {
            throw new BusinessException("请假记录不存在");
        }

        leave.setStatus(status);
        leave.setAuditRemark(remark);
        leave.setAuditTime(java.time.LocalDateTime.now());

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
}
