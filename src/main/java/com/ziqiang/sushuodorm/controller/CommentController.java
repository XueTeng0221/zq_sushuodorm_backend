package com.ziqiang.sushuodorm.controller;

import com.ziqiang.sushuodorm.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("评论")
@AllArgsConstructor
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "添加评论")
    @GetMapping("comment")
    public boolean addComment(Long postId) {
        return commentService.addComment(postId);
    }

    @Operation(summary = "添加回复")
    @GetMapping("reply")
    public boolean addReply(Long commentId) {
        return commentService.addReply(commentId);
    }

    @Operation(summary = "点赞评论")
    @GetMapping("like")
    public boolean likeComment(Long commentId) {
        return commentService.likeComment(commentId);
    }

    @Operation(summary = "删除评论")
    @GetMapping("delete")
    public boolean deleteComment(Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
