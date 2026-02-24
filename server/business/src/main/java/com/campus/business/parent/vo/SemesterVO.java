package com.campus.business.parent.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学期信息 VO
 */
@Data
public class SemesterVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private Integer status;
    private Integer isCurrent;
}
