package com.ziqiang.sushuodorm.entity.dto.like;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikePostAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long postId;
}
