package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName(value = "favorite")
public class FavoriteItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long postId;

    private Long userId;

    private String username;

    @TableField
    private java.sql.Date createTime;
    @TableField
    private Date updateTime;

    private Boolean isDeleted;


}
