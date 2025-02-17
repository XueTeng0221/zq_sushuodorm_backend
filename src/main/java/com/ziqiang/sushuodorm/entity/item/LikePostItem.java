package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "likePost")
public class LikePostItem implements Serializable {
    @TableField(value = "serialVersionUID", fill = FieldFill.INSERT)
    private static final long serialVersionUID = 1L;
    @TableField
    private Date date;
    @TableField
    private String username;
    @TableField
    private String userId;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField
    private Long postId;
    @TableLogic
    private Boolean isDeleted;
}
