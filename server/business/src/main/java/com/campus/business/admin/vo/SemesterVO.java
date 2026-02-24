package com.campus.business.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学期信息 VO
 */
@Data
public class SemesterVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private String statusName;
    private Integer isCurrent;
    private String currentName;
    private java.time.LocalDateTime createTime;
}
