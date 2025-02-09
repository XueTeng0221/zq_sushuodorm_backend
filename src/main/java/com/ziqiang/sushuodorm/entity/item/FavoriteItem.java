package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
@TableName(value = "favorite")
public class FavoriteItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "post_id")
    private Long postId;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "username")
    private String username;
    @TableField
    private Date createTime;
    @TableField
    private Date updateTime;
    @TableLogic
    private Boolean isDeleted;
}
