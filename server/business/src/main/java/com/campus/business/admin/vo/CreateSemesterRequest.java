package com.campus.business.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 创建/更新学期请求 VO
 */
@Data
public class CreateSemesterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "学期名称不能为空")
    private String name;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    private Integer status;
    private Integer isCurrent;
}
