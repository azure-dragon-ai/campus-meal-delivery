package com.campus.business.teacher.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 教师注册请求 VO
 */
@Data
public class TeacherRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private Long schoolId;
    private Long classId;
    private String position;
}
