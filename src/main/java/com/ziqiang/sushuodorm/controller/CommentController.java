package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.services.CommentService;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
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
    public boolean addComment(@RequestParam Long postId, @RequestParam Long userId,
                              @RequestBody CommentItem parent, @RequestParam String content) {
        return commentService.addComment(postId, userId, content);
    }

    @Operation(summary = "添加回复")
    @PostMapping("reply")
    public boolean addReply(@RequestParam Long commentId, @RequestParam Long userId, @RequestParam String content) {
        return commentService.addReply(commentId, userId, content);
    }

    @Operation(summary = "点赞评论")
    @PostMapping("like")
    public boolean likeComment(@RequestParam Long commentId) {
        return commentService.likeComment(commentId);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("delete")
    public boolean deleteComment(@RequestParam Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @Operation(summary = "获取评论")
    @GetMapping("getComment")
    public CommentVo getComment(@RequestParam Long commentId) {
        return commentService.getComment(commentId);
    }

    @Operation(summary = "获取所有评论")
    @GetMapping("getAllComments")
    public List<CommentVo> getAllComments(@RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllComments(queryRequest).getRecords();
    }

    @Operation(summary = "获取所有回复")
    @GetMapping("getAllReplies")
    public List<CommentVo> getAllReplies(@RequestBody CommentQueryRequest queryRequest) {
        return commentService.getAllReplies(queryRequest).getRecords();
    }

    @Operation(summary = "获取用户的所有评论")
    @GetMapping("getAllCommentsByUsername")
    public List<CommentVo> getAllCommentsByUsername(@RequestParam String username) {
        return commentService.getAllCommentsByUsername(username).getRecords();
    }

    @Operation(summary = "获取用户的所有回复")
    @GetMapping("getAllRepliesByUsername")
    public List<CommentVo> getAllRepliesByUsername(@RequestParam String replierName, @RequestParam String username) {
        return commentService.getAllRepliesByUsername(replierName, username).getRecords();
    }
}