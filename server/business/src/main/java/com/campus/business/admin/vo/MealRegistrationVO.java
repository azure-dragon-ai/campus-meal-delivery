package com.campus.business.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 配餐登记信息 VO
 */
@Data
public class MealRegistrationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long semesterId;
    private String semesterName;
    private Long dinerId;
    private String dinerName;
    private String idCard;
    private String phone;
    private Long schoolId;
    private String schoolName;
    private Long classId;
    private String className;
    private Integer isEat;
    private String isEatName;
    private String remark;
    private java.time.LocalDateTime createTime;
}
