package com.campus.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.database.entity.SysAdmin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员 Mapper
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {
}
