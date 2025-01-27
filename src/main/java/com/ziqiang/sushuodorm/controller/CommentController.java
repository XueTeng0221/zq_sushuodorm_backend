package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("评论")
@AllArgsConstructor
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "添加评论")
    @PostMapping("comment")
    public boolean addComment(@RequestParam Long postId, @RequestParam String username, @RequestParam String content) {
        return commentService.addComment(postId, username, content);
    }

    @Operation(summary = "添加回复")
    @PostMapping("reply")
    public boolean addReply(@RequestParam Long commentId, @RequestParam String username, @RequestParam String content) {
        return commentService.addReply(commentId, username, content);
    }

    @Operation(summary = "点赞评论")
    @PostMapping("like")
    public boolean likeComment(@RequestParam Long commentId) {
        return commentService.likeComment(commentId);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("delete")
    public boolean deleteComment(@RequestParam Long commentId, @RequestParam Long postId) {
        return commentService.deleteComment(commentId, postId);
    }

    @Operation(summary = "获取评论")
    @GetMapping("getComment")
    public CommentVo getComment(@RequestParam Long commentId) {
        return commentService.getComment(commentId);
    }

    @Operation(summary = "获取所有评论")
    @GetMapping("getAllComments")
    public List<CommentVo> getAllComments(@RequestParam String author, @RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllComments(author, queryRequest).getRecords();
    }

    @Operation(summary = "由关键词获取所有评论")
    @GetMapping("getAllCommentsByKeywords")
    public List<CommentVo> getAllCommentsByKeywords(@RequestParam List<String> keywords, @RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllComments(keywords, queryRequest).getRecords();
    }

    @Operation(summary = "获取所有回复")
    @GetMapping("getAllReplies")
    public List<CommentVo> getAllReplies(@RequestParam String username,
                                         @RequestParam Long commentId, @RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllReplies(username, commentId, queryRequest).getRecords();
    }

    @Operation(summary = "获取用户回复")
    @GetMapping("getAllRepliesByUser")
    public List<CommentVo> getAllRepliesByUser(@RequestParam String replierName,
                                               @RequestParam String username, @RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllReplies(replierName, username, queryRequest).getRecords();
    }

    @Operation(summary = "获取评论")
    @GetMapping("getAllComments")
    public List<CommentItem> getAllComments(@RequestParam Long postId, @RequestBody CommentQueryRequest queryRequest) {
        return commentService.findComments(postId, queryRequest);
    }

    @Operation(summary = "获取用户的所有评论")
    @GetMapping("getAllCommentsByUsername")
    public List<CommentItem> getAllCommentsByUsername(@RequestParam String username) {
        return commentService.findCommentsByUsername(username);
    }

    @Operation(summary = "获取评论回复数")
    @GetMapping("getReplyCount")
    public int getReplyCount(@RequestParam Long commentId, @RequestParam Long postId) {
        return commentService.getReplyCount(commentId, postId);
    }
}