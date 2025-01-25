package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "likeComment")
public class LikeCommentItem implements Serializable {
    @TableField(value = "serialVersionUID", fill = FieldFill.INSERT)
    private static final long serialVersionUID = 1L;

    private Date date;

    private String username;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long commentId;

    private Long userId;
}