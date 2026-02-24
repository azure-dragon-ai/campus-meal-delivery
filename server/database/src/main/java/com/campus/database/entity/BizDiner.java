package com.campus.database.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 就餐人实体
 */
@Data
@TableName("biz_diner")
public class BizDiner implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long parentId;
    private Long schoolId;
    private Long classId;
    private String name;
    private Integer role;
    private String idCard;
    private String phone;
    private String bankAccountName;
    private String bankName;
    private String bankSubbranch;
    private String bankAccount;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
