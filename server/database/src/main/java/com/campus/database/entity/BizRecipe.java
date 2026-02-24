package com.campus.database.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 食谱实体
 */
@Data
@TableName("biz_recipe")
public class BizRecipe implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long semesterId;
    private Long schoolId;
    private LocalDate date;
    private Integer weekDay;
    private String lunchMenu;
    private String lunchMenuWithWeight;
    private String dinnerMenu;
    private String dinnerMenuWithWeight;
    private Long createBy;
    private String createName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
