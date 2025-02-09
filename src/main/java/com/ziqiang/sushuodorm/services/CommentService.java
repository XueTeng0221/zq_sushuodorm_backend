package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;

import java.sql.Date;
import java.util.List;

public interface CommentService extends IService<CommentItem> {

    boolean addComment(Date date, Long postId, String username, String content);

    boolean addReply(Date date, Long commentId, String username, String content);

    boolean deleteComment(Long commentId, Long postId);

    CommentVo getComment(Long commentId);

    IPage<CommentVo> getAllComments(String author, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllComments(List<String> keywords, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllRepliesByCommentId(String username, Long postId, Long commentId, CommentQueryRequest queryRequest);

    IPage<CommentVo> getAllRepliesByUser(String replierName, String username, Long postId, CommentQueryRequest queryRequest);

    int getReplyCount(Long commentId, Long postId);

    List<CommentItem> findComments(Long postId, CommentQueryRequest queryRequest);

    List<CommentItem> findCommentsByUsername(String username);
}