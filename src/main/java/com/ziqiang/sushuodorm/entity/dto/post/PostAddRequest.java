package com.ziqiang.sushuodorm.entity.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
public class PostAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private List<String> tags;
    private Date createDate;
}
