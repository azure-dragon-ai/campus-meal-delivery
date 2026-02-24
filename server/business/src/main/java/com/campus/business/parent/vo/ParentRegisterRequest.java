package com.campus.business.parent.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 家长注册请求 VO
 */
@Data
public class ParentRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;
}
