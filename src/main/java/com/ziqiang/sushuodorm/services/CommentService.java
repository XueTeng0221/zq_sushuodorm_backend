package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;

import java.util.List;

public interface CommentService extends IService<CommentItem> {
    String addComment(String content);

    String addComment(Long commentId);

    boolean likeComment(Long commentId);

    boolean deleteComment(Long commentId);

    CommentVo getComment(Long commentId);

    IPage<CommentVo> getAllComments(CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllComments(Long postId, int pageNum, int pageId);

    IPage<CommentVo> getAllReplies(CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllReplies(Long commentId, int pageNum, int pageId);

    IPage<CommentVo> getAllCommentsByUsername(String username);

    IPage<CommentVo> getAllRepliesByUsername(String replierName, String username);

    List<CommentItem> findComments(CommentQueryRequest queryRequest);

    List<CommentItem> findCommentsByUsername(CommentQueryRequest queryRequest, String username);

    CommentItem replyComment(Long parentId, CommentItem commentItem);
}