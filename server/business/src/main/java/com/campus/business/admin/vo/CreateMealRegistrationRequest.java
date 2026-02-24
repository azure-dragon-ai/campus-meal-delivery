package com.campus.business.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建配餐登记请求 VO
 */
@Data
public class CreateMealRegistrationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学期不能为空")
    private Long semesterId;

    @NotNull(message = "就餐人不能为空")
    private Long dinerId;

    @NotNull(message = "学校不能为空")
    private Long schoolId;

    private Long classId;
    private String dinerName;
    private String idCard;
    private String phone;

    @NotNull(message = "是否在校吃配餐不能为空")
    private Integer isEat;

    private String remark;
}
