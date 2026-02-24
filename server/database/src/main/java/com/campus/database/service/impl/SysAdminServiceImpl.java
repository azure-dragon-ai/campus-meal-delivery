package com.campus.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.database.entity.SysAdmin;
import com.campus.database.mapper.SysAdminMapper;
import com.campus.database.service.SysAdminService;
import org.springframework.stereotype.Service;

/**
 * 管理员服务实现
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {
}
