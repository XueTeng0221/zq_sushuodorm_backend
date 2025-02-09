package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@TableName("post")
public class PostItem implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private Set<CommentItem> comments;
    @TableField(exist = false)
    private Date date;
    @TableField(value = "title", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String title;
    @TableField(value = "content", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String content;
    @TableField(value = "author", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String author;
    @TableField(value = "tags", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tags;
    @TableField(value = "likes")
    private Long likes;
    @TableField(value = "favorites")
    private Long favorites;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableLogic
    private Boolean isDeleted;
}