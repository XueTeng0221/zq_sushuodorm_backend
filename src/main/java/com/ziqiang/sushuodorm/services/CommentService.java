package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;

import java.util.List;

public interface CommentService extends IService<CommentItem> {

    boolean addComment(Long postId, String username, String content);

    boolean addReply(Long commentId, String username, String content);

    boolean likeComment(Long commentId);

    boolean deleteComment(Long commentId, Long postId);

    CommentVo getComment(Long commentId);

    IPage<CommentVo> getAllComments(String author, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllComments(List<String> keywords, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllReplies(String username, Long commentId, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllReplies(String replierName, String username, CommentQueryRequest queryRequest);

    int getReplyCount(Long commentId, Long postId);

    List<CommentItem> findComments(Long postId, CommentQueryRequest queryRequest);

    List<CommentItem> findCommentsByUsername(String username);
}