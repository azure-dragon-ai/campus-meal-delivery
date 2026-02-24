package com.campus.business.teacher.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 就餐人信息 VO
 */
@Data
public class DinerVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Integer role;
    private String roleName;
    private String idCard;
    private String phone;
    private Long schoolId;
    private String schoolName;
    private Long classId;
    private String className;
    private String bankAccountName;
    private String bankName;
    private String bankSubbranch;
    private String bankAccount;
    private Integer status;
}
