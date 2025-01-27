package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Data
@Accessors(chain = true)
@TableName(value = "comment")
public class CommentItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField
    private Set<CommentItem> replies;
    @TableField(value = "content")
    private String content;
    @TableField(value = "author")
    private String author;
    @TableField(value = "date")
    private Date date;
    @TableField(value = "likes")
    private Long likes;
    @TableField(value = "post_id")
    private Long postId;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "parent_id")
    private Long parentId;
    @TableId(type = IdType.AUTO)
    private Long replyNum;
}
