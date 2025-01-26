package com.ziqiang.sushuodorm.entity.dto.comment;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziqiang.sushuodorm.common.PageRequest;
import com.ziqiang.sushuodorm.entity.dto.post.PostQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private PostQueryRequest postQueryRequest;
    private Page<CommentItem> page;
    private Wrapper<CommentItem> queryWrapper;
    private Long userId;
    private Long postId;
    private Long commentId;
    private String username;
    private String content;
    private String sortField;
    private Integer replyNum;
}
