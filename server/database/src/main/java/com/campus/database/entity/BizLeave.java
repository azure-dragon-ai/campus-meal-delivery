package com.campus.database.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假实体
 */
@Data
@TableName("biz_leave")
public class BizLeave implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long dinerId;
    private String dinerName;
    private String phone;
    private LocalDate leaveDate;
    private String reason;
    private Integer status;
    private LocalDateTime auditTime;
    private String auditRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
