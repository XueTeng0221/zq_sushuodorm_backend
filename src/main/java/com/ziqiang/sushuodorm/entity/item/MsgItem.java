package com.ziqiang.sushuodorm.entity.item;

import java.io.Serializable;
import java.sql.Date;

public class MsgItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date date;
    private String content;
    private String author;
    private Long id;
}
