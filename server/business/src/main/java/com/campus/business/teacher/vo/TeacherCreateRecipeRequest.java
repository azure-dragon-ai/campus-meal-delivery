package com.campus.business.teacher.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 创建食谱请求 VO（教师）
 */
@Data
public class TeacherCreateRecipeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校不能为空")
    private Long schoolId;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    @NotNull(message = "星期不能为空")
    private Integer weekDay;

    private String lunchMenu;
    private String lunchMenuWithWeight;
    private String dinnerMenu;
    private String dinnerMenuWithWeight;
}
