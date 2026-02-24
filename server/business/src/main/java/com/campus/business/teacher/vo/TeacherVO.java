package com.campus.business.teacher.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师信息 VO
 */
@Data
public class TeacherVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String phone;
    private Long schoolId;
    private String schoolName;
    private Long classId;
    private String className;
    private String position;
    private Integer status;
    private LocalDateTime createTime;
}
