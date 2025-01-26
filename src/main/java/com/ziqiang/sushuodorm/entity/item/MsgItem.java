package com.ziqiang.sushuodorm.entity.item;

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

    private Date date;

    private String content;

    private String author;

    private Long id;
}
