package com.campus.business.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 管理员信息 VO
 */
@Data
public class AdminVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private java.time.LocalDateTime lastLoginTime;
    private java.time.LocalDateTime createTime;
}
