package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "likeComment")
public class LikeCommentItem implements Serializable {
    @TableField(value = "serialVersionUID", fill = FieldFill.INSERT)
    private static final long serialVersionUID = 1L;
    @TableField(value = "date", fill = FieldFill.INSERT)
    private Date date;
    @TableField(value = "username")
    private String username;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "comment_id")
    private Long commentId;
    @TableField(value = "user_id")
    private Long userId;
}