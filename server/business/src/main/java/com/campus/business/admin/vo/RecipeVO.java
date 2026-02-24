package com.campus.business.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 食谱信息 VO
 */
@Data
public class RecipeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long semesterId;
    private String semesterName;
    private Long schoolId;
    private String schoolName;
    private LocalDate date;
    private Integer weekDay;
    private String weekDayName;
    private String lunchMenu;
    private String lunchMenuWithWeight;
    private String dinnerMenu;
    private String dinnerMenuWithWeight;
    private Long createBy;
    private String createName;
    private java.time.LocalDateTime createTime;
}
