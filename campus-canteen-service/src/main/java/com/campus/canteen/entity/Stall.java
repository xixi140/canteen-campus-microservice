package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 档口实体 — canteen_db.stall 表
 */
@Data
@TableName("stall")
public class Stall {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long canteenId;          // 所属食堂ID
    private String name;             // 档口名称：1号窗口、面食窗口
    private String category;         // 分类：炒菜/面食/小吃/饮品
    private Integer sortOrder;       // 排序

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
