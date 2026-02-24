package com.campus.business.parent.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假信息 VO
 */
@Data
public class LeaveVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long dinerId;
    private String dinerName;
    private String phone;
    private LocalDate leaveDate;
    private String reason;
    private Integer status;
    private String statusName;
    private LocalDateTime auditTime;
    private String auditRemark;
    private LocalDateTime createTime;
}
