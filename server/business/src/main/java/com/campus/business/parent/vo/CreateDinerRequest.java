package com.campus.business.parent.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 创建就餐人请求 VO
 */
@Data
public class CreateDinerRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "角色不能为空")
    private Integer role;

    private String idCard;
    private String phone;

    @NotNull(message = "学校不能为空")
    private Long schoolId;

    private Long classId;

    private String bankAccountName;
    private String bankName;
    private String bankSubbranch;
    private String bankAccount;
}
