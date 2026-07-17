package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 食堂实体 — canteen_db.canteen 表
 */
@Data
@TableName("canteen")
public class Canteen {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;             // 食堂名称：一食堂、二食堂
    private String location;         // 位置描述
    private String openTime;         // 营业时间
    private String imageUrl;         // 封面图
    private Integer stallCount;      // 档口数量

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
