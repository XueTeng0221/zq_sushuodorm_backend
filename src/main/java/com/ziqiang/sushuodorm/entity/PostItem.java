package com.ziqiang.sushuodorm.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

import java.sql.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("post")
public class PostItem {
    private List<CommentItem> comments;
    private Date date;
    private String title;
    private String content;
    private String author;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer likes;
}
