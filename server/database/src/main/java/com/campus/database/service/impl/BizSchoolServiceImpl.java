package com.campus.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.database.entity.BizSchool;
import com.campus.database.mapper.BizSchoolMapper;
import com.campus.database.service.BizSchoolService;
import org.springframework.stereotype.Service;

/**
 * 学校服务实现
 */
@Service
public class BizSchoolServiceImpl extends ServiceImpl<BizSchoolMapper, BizSchool> implements BizSchoolService {
}
