package com.ziqiang.sushuodorm.entity.dto.like;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class LikeCommentAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date date;
    private Long commentId;
}
