package com.ziqiang.sushuodorm.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

@Data
@Accessors(chain = true)
@TableName("msg")
public class MsgItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableField(value = "date")
    private Date date;
    @TableField(value = "content")
    private String content;
    @TableField(value = "author")
    private String author;
    @TableField(value = "id")
    private Long id;
}
