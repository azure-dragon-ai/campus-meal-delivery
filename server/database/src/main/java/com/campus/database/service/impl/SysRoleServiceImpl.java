package com.campus.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.database.entity.SysRole;
import com.campus.database.mapper.SysRoleMapper;
import com.campus.database.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
