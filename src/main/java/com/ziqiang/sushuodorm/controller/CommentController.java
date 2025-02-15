package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.entity.vo.ResponseBeanVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.sql.Date;

@RestController
@RequestMapping("comment")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "添加评论")
    @PostMapping("comment")
    public ResponseBeanVo<?> addComment(@RequestParam Date date, @RequestParam Long postId, @RequestParam String username, @RequestParam String content) {
        boolean b = commentService.addComment(date, postId, username, content);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "添加回复")
    @PostMapping("reply")
    public ResponseBeanVo<?> addReply(@RequestParam Date date, @RequestParam Long commentId, @RequestParam String username, @RequestParam String content) {
        boolean b = commentService.addReply(date, commentId, username, content);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("delete")
    public ResponseBeanVo<?> deleteComment(@RequestParam Long commentId, @RequestParam Long postId) {
        boolean b = commentService.deleteComment(commentId, postId);
        return b ? ResponseBeanVo.ok() : ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
    }

    @Operation(summary = "获取评论")
    @GetMapping("getComment")
    public ResponseBeanVo<CommentVo> getComment(@RequestParam Long commentId) {
        return ResponseBeanVo.ok(commentService.getComment(commentId));
    }

    @Operation(summary = "获取所有评论")
    @GetMapping("getAllComments")
    public ResponseBeanVo<List<CommentVo>> getAllComments(@RequestParam String author,
                                                          @RequestBody CommentQueryRequest queryRequest) {
        return ResponseBeanVo.ok(commentService.getAllComments(author, queryRequest).getRecords());
    }

    @Operation(summary = "由关键词获取所有评论")
    @GetMapping("getAllCommentsByKeywords")
    public ResponseBeanVo<List<CommentVo>> getAllCommentsByKeywords(@RequestParam List<String> keywords,
                                                    @RequestBody CommentQueryRequest queryRequest) {
        return ResponseBeanVo.ok(commentService.getAllComments(keywords, queryRequest).getRecords());
    }

    @Operation(summary = "获取所有回复")
    @GetMapping("getAllReplies")
    public ResponseBeanVo<?> getAllRepliesByCommentId(@RequestParam String username, @RequestParam Long commentId,
                                                                    @RequestParam Long postId, @RequestBody CommentQueryRequest queryRequest) {
        try {
            return ResponseBeanVo.ok(commentService.getAllRepliesByCommentId(username, postId, commentId, queryRequest).getRecords());
        } catch (NoSuchPostException e) {
            log.debug("getAllRepliesByCommentId: {}", e.getMessage());
            return ResponseBeanVo.error(ErrorCode.CLIENT_ERROR, null);
        }
    }

    @Operation(summary = "获取用户回复")
    @GetMapping("getAllRepliesByUser")
    public ResponseBeanVo<List<CommentVo>> getAllRepliesByUser(@RequestParam String replierName, @RequestParam String username,
                                                               @RequestParam Long postId, @RequestBody CommentQueryRequest queryRequest) {
        return ResponseBeanVo.ok(commentService.getAllRepliesByUser(replierName, username, postId, queryRequest).getRecords());
    }

    @Operation(summary = "获取评论")
    @GetMapping("getAllComments")
    public ResponseBeanVo<List<CommentItem>> getAllComments(@RequestParam Long postId,
                                                            @RequestBody CommentQueryRequest queryRequest) {
        return ResponseBeanVo.ok(commentService.findComments(postId, queryRequest));
    }

    @Operation(summary = "获取用户的所有评论")
    @GetMapping("getAllCommentsByUsername")
    public ResponseBeanVo<List<CommentItem>> getAllCommentsByUsername(@RequestParam String username) {
        return ResponseBeanVo.ok(commentService.findCommentsByUsername(username));
    }

    @Operation(summary = "获取评论回复数")
    @GetMapping("getReplyCount")
    public ResponseBeanVo<Integer> getReplyCount(@RequestParam Long commentId, @RequestParam Long postId) {
        return ResponseBeanVo.ok(commentService.getReplyCount(commentId, postId));
    }
}