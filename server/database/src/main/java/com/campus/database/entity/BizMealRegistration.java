package com.campus.database.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 配餐登记实体
 */
@Data
@TableName("biz_meal_registration")
public class BizMealRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long semesterId;
    private Long dinerId;
    private Long schoolId;
    private Long classId;
    private String dinerName;
    private String idCard;
    private String phone;
    private Integer isEat;
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
