package com.ziqiang.sushuodorm.entity.dto.favorite;

import lombok.Data;

import java.io.Serializable;

@Data
public class FavoriteAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long postId;
}
