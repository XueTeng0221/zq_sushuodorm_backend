package com.ziqiang.sushuodorm.entity.dto.post;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -132458973245893245L;
    private Page<PostItem> page;
    private Long id;
    private Long notId;
    private String searchText;
    private String title;
    private String content;
    private List<String> tags;
    private List<String> orTags;
    private Long userId;
    private Long favorUserId;
    private Long likeUserId;
}
