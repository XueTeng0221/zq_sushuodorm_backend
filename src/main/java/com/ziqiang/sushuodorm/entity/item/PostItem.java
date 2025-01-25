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

    private Set<CommentItem> comments;

    private Date date;

    @TableField(value = "title", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String title;

    @TableField(value = "content", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String content;

    @TableField(value = "author", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String author;

    @TableField(value = "tags", updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tags;

    @TableField
    private Long likes;

    @TableField
    private Long favorites;

    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    @TableLogic
    private Integer isDeleted;
}