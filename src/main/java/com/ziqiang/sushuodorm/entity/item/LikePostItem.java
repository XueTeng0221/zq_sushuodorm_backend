package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "likePost")
public class LikePostItem implements Serializable {
    @TableField(value = "serialVersionUID", fill = FieldFill.INSERT)
    private static final long serialVersionUID = 1L;

    @TableField
    private Date date;

    @TableField
    private String username;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private Long postId;

    @TableField
    private Long userId;
}
