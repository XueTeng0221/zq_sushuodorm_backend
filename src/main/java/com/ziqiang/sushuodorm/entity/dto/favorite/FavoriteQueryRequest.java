package com.ziqiang.sushuodorm.entity.dto.favorite;

import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.dto.post.PostQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class FavoriteQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private PostQueryRequest postQueryRequest;
    private Long userId;
}
