package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
@TableName(value = "comment")
public class CommentItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    private String author;

    private Date date;

    private Long likes;

    private Long postId;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long floor;

    private Long parentId;

    private Long replies;
}
