package com.campus.business.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.business.admin.service.AdminService;
import com.campus.business.admin.vo.*;
import com.campus.core.common.PageResult;
import com.campus.core.exception.BusinessException;
import com.campus.core.exception.UnauthorizedException;
import com.campus.core.util.JwtUtil;
import com.campus.core.util.PasswordUtil;
import com.campus.database.entity.SysAdmin;
import com.campus.database.entity.SysMenu;
import com.campus.database.entity.SysRole;
import com.campus.database.mapper.SysAdminMapper;
import com.campus.database.mapper.SysMenuMapper;
import com.campus.database.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台管理服务实现
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final SysAdminMapper sysAdminMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 查询管理员
        SysAdmin admin = sysAdminMapper.selectOne(new LambdaQueryWrapper<SysAdmin>()
                .eq(SysAdmin::getUsername, request.getUsername()));

        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查状态
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成 Token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", admin.getId());
        payload.put("userType", "ADMIN");
        payload.put("username", admin.getUsername());
        String token = JwtUtil.createToken(payload);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(admin.getId());
        response.setUsername(admin.getUsername());
        response.setName(admin.getName());
        response.setAvatar(admin.getAvatar());

        // 获取角色
        List<SysRole> roles = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, 1));
        response.setRoles(roles.stream().map(SysRole::getCode).collect(Collectors.toList()));

        // 获取菜单
        List<SysMenu> menus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getVisible, 1)
                .orderByAsc(SysMenu::getSort));
        response.setMenus(buildMenuTree(menus));

        return response;
    }

    @Override
    public void logout(Long adminId) {
        // JWT 无状态，此处可记录黑名单
    }

    @Override
    public PageResult<AdminVO> getAdminList(Integer page, Integer size, String keyword) {
        Page<SysAdmin> adminPage = new Page<>(page, size);
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w
                    .like(SysAdmin::getUsername, keyword)
                    .or()
                    .like(SysAdmin::getName, keyword)
                    .or()
                    .like(SysAdmin::getPhone, keyword));
        }
        wrapper.orderByDesc(SysAdmin::getCreateTime);

        Page<SysAdmin> result = sysAdminMapper.selectPage(adminPage, wrapper);

        List<AdminVO> records = result.getRecords().stream().map(admin -> {
            AdminVO vo = new AdminVO();
            vo.setId(admin.getId());
            vo.setUsername(admin.getUsername());
            vo.setName(admin.getName());
            vo.setPhone(admin.getPhone());
            vo.setEmail(admin.getEmail());
            vo.setAvatar(admin.getAvatar());
            vo.setStatus(admin.getStatus());
            vo.setLastLoginTime(admin.getLastLoginTime());
            vo.setCreateTime(admin.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.of(records, result.getTotal(), size, page);
    }

    @Override
    public Long createAdmin(CreateAdminRequest request) {
        // 检查用户名是否存在
        Long count = sysAdminMapper.selectCount(new LambdaQueryWrapper<SysAdmin>()
                .eq(SysAdmin::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysAdmin admin = new SysAdmin();
        admin.setUsername(request.getUsername());
        admin.setPassword(PasswordUtil.encode(request.getPassword()));
        admin.setName(request.getName());
        admin.setPhone(request.getPhone());
        admin.setEmail(request.getEmail());
        admin.setAvatar(request.getAvatar());
        admin.setStatus(1);

        sysAdminMapper.insert(admin);
        return admin.getId();
    }

    @Override
    public void updateAdmin(Long id, CreateAdminRequest request) {
        SysAdmin admin = sysAdminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        admin.setName(request.getName());
        admin.setPhone(request.getPhone());
        admin.setEmail(request.getEmail());
        admin.setAvatar(request.getAvatar());

        sysAdminMapper.updateById(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        sysAdminMapper.deleteById(id);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        SysAdmin admin = sysAdminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        admin.setPassword(PasswordUtil.encode(newPassword));
        sysAdminMapper.updateById(admin);
    }

    private List<LoginResponse.MenuVO> buildMenuTree(List<SysMenu> menus) {
        List<LoginResponse.MenuVO> roots = new ArrayList<>();
        Map<Long, LoginResponse.MenuVO> map = new HashMap<>();

        for (SysMenu menu : menus) {
            LoginResponse.MenuVO vo = new LoginResponse.MenuVO();
            vo.setId(menu.getId());
            vo.setParentId(menu.getParentId());
            vo.setName(menu.getName());
            vo.setPath(menu.getPath());
            vo.setComponent(menu.getComponent());
            vo.setIcon(menu.getIcon());
            vo.setType(menu.getType());
            vo.setPermission(menu.getPermission());
            vo.setSort(menu.getSort());
            vo.setChildren(new ArrayList<>());
            map.put(menu.getId(), vo);
        }

        for (LoginResponse.MenuVO vo : map.values()) {
            if (vo.getParentId() == 0) {
                roots.add(vo);
            } else {
                LoginResponse.MenuVO parent = map.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }

        roots.sort(Comparator.comparingInt(LoginResponse.MenuVO::getSort));
        return roots;
    }
}
