package com.campus.business.parent.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 创建请假请求 VO
 */
@Data
public class CreateLeaveRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "就餐人不能为空")
    private Long dinerId;

    @NotNull(message = "请假日期不能为空")
    private LocalDate leaveDate;

    private String reason;
}
