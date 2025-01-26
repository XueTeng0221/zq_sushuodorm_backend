package com.ziqiang.sushuodorm.entity.dto.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long postId;
    private Long userId;
    private Long commentId;
    private String content;
    private Integer replyNum;
}
